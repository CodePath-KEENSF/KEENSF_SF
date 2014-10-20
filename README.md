KEENSF_SF
=========

**What problem does this app address**

KEEN SF is a part of national, nonprofit organization that provides one-to-one-volunteer-led recreational opportunities for children and young adults with developmental delays or physical disabilities at no cost to their families and caregivers. KEEN’s mission is to foster the self-esteem, confidence, skills and talents of its athletes through non-competitive activities, allowing young people facing even the most significant challenges to meet their individual goals.

Currently KEEN relies on their website to register new participants and new volunteers as well as register approved volunteers for an upcoming session. All sessions are one-to-one so its is mandatory to pre-register and be mindful about cancellations. However it is hard to track attendance and athletes progress as coaches and participants cannot be easily check-in for the sessions on the spot. Additionally updating athlete and volunteer profile information is not streamlined at the moment. It requires entering the data by a staff member with appropriate profile access level. There is only one paid staff member and his/her time could be more effectively managed for outreach and program development instead of checking–in and entering data. Furthermore there is a critical need to get parents and volunteers more engaged and better informed about sessions, athlete’s progress feedback and upcoming events.

**How does this app will solve that problem**

The app should provide an easy and efficient way to check-in coaching volunteers and athletes/parents for a session as well as to update their profile information. Furthermore an app could help with sending SMS/email/push notification updates, reminders and info to parents and volunteers.

The initial set of epics is listed below:

**REQUIRED**

1.	User should be able to **_login to KEEN MOBILE_**. As per Civicore one user account is for one affiliate (e.g. KEEN SF) and related data access. If existing  API access key is found then user is not asked to login and Session List screen is loaded. If API access key is not found then user is asked to enter his/her user name and password. Then Civicore API generates an API access key for a user on authentication request. No sign-up should be available.
2.	User should be able to **_view sessions for today or next available day_** if applicable. Session list should be the main landing screen for a logged user. Session should be grouped by location and ordered by session time within the location group. For each session the following info should be displayed: program name, time, number of registered athletes and number of registered coaches (plus location and date info displayed on the screen). 
3.	User should be able to **_view session details_** by clicking on a session in session list. The following information for a session should be displayed: date, times, program name, location, registered coach list and registered athlete list.
4.	User should be able to **_click on a registered participant in session details screen and navigate to relevant profile page_**.
5.	User should be able to **_register a participant for a session_**. User will search for a coach or athlete by name/surname/phone number and add him/her to a registered coach/athlete list for a session.
6.	User should be able to **_update attendance status of participant(s) for a session_**. Existing attendance statuses and rules should be applied.
7.	User should be able to **_see a list of coaches and search for a coach by name/phone_**. Coaches should be ordered with the most recently attended sessions first.
8.	User should be able to **_contact a coach via sms, email or phone call from the profile screen_** providing that the phone/email for a coach exist.
9.	User should be able to **_see a list of athletes and search for an athlete by name/phone_**. Athletes should be ordered with the most recently attended sessions first. 
10.	User should be able to **_contact an athlete’s primary parent via sms, email or phone call from the profile screen_** providing that the phone/email for a primary parent exist.
11.	User should be able to **_update contact info for a coach_**
12.	User should be able to **_update contact info for an athlete’s primary parent_**

**OPTIONAL**

13.	User should be able to **_view session for a selected date in the past or in future_**
14.	User should be able to **_to filter sessions based on location or program_**. Filtering preferences may be saved for a user.
15.	User should be able to **_create a new session_**. Date and program should be specified. On session creation **_athletes that enrolled in the program should get notified by email/sms_**.
16.	User should be able to **_cancel an upcoming session_**. On session cancelation all relevant **_participants that are registered for the session should get notified by email/sms_**.
17	**_Athletes and coaches should be reminded about an upcoming session_** that they are registered to by email/sms.
18.	User should be able to **_create and send bulk sms/email to a selected participants_**.
19.	Coach user should be able to **_login to KEEN MOBILE, view upcoming sessions, register for a session, cancel registration for a session and update own contact info_**.
20.	Athlete’s parent/guardian user should be able to **_login to KEEN MOBILE, view upcoming sessions, notify about absence and update own contact info_**.


Walkthrough of all users stories:

![Video Walkthrough](KeenSF.gif)

GIF created with [LiceCap]

[LiceCap]:www.cockos.com/licecap/
