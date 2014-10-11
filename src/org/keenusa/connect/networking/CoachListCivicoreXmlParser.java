package org.keenusa.connect.networking;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.keenusa.connect.models.Coach;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class CoachListCivicoreXmlParser {

	private static final String ns = null;

	public List<Coach> parse(InputStream in) throws XmlPullParserException, IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readFeed(parser);
		} finally {
			in.close();
		}
	}

	private List<Coach> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
		List<Coach> coaches = new ArrayList<Coach>();

		parser.require(XmlPullParser.START_TAG, ns, "results");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("row")) {
				coaches.add(readRow(parser));
			} else {
				skip(parser);
			}
		}
		return coaches;
	}

	// Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
	// to their respective "read" methods for processing. Otherwise, skips the tag.
	private Coach readRow(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "row");
		Coach coach = new Coach();
		String rowId = parser.getAttributeValue(null, "id");
		coach.setRemoteId(Long.valueOf(rowId));

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("firstName")) {
				coach.setFirstName(readFirstName(parser));
			} else if (name.equals("lastName")) {
				coach.setLastName(readLastName(parser));
			} else {
				skip(parser);
			}
		}
		return coach;
	}

	private String readFirstName(XmlPullParser parser) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "firstName");
		String firstName = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "firstName");
		return firstName;
	}

	private String readLastName(XmlPullParser parser) throws IOException, XmlPullParserException {
		String link = "";
		parser.require(XmlPullParser.START_TAG, ns, "lastName");
		String tag = parser.getName();
		String relType = parser.getAttributeValue(null, "rel");
		if (tag.equals("link")) {
			if (relType.equals("alternate")) {
				link = parser.getAttributeValue(null, "href");
				parser.nextTag();
			}
		}
		parser.require(XmlPullParser.END_TAG, ns, "link");
		return link;
	}

	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}

}