KEENSF_SF
=========

**What problem does this app address**

KEEN SF is a part of national, nonprofit organization that provides one-to-one-volunteer-led recreational opportunities for children and young adults with developmental delays or physical disabilities at no cost to their families and caregivers. KEEN’s mission is to foster the self-esteem, confidence, skills and talents of its athletes through non-competitive activities, allowing young people facing even the most significant challenges to meet their individual goals.

Currently KEEN relies on their website to register new participants and new volunteers as well as register approved volunteers for an upcoming session. All sessions are one-to-one so its is mandatory to pre-register and be mindful about cancellations. However it is hard to track attendance and athletes progress as coaches and participants cannot be easily check-in for the sessions on the spot. Additionally updating athlete and volunteer profile information is not streamlined at the moment. It requires entering the data by a staff member with appropriate profile access level. There is only one paid staff member and his/her time could be more effectively managed for outreach and program development instead of checking–in and entering data. Furthermore there is a critical need to get parents and volunteers more engaged and better informed about sessions, athlete’s progress feedback and upcoming events.

**How does this app will solve that problem**

The app should provide an easy and efficient way to check-in coaching volunteers and athletes/parents for a session as well as to update their profile information. Furthermore an app could help with sending SMS/email/push notification updates, reminders and info to parents and volunteers.

The initial set of epics is listed below:

**REQUIRED**

1.	As a staff member I want to **_login to KEEN MOBILE_** so that I can get access to required information and actions.
2.	As a staff member I want to **_register and check-in a coach for a session_** on the spot so that attendance is tracked properly.
3.	As a staff member I want to **_check-in an athlete for a session_** on the spot so that attendance is tracked properly.
4.	As a staff member I want to **_mark an athlete as absent from a session_** on the spot so that attendance is tracked properly.
5.	As a staff member I want to **_update important coach profile data_** on the spot so that I do not forget to do it later on the web.
6.	As a staff member I want to **_update important athlete profile data_** on the spot so that I do not forget to do it later on the web.
7.	As a staff member I want to **_notify relevant athletes and coaches about a cancelled session_** so that they know about it.
8.	As a staff member I want to **_notify relevant athletes and coaches about a new session_** so that they know about it and coaches can register.
9.	As a staff member I want **_athletes and coaches to be reminded about an upcoming session_** that they are registered to so that they do not forget about it.

**OPTIONAL**

10.	As a coach I want to **_login to KEEN MOBILE_** so that I can get access to required information and actions.
11.	As a coach I want to **_check-in my athlete and myself for a session_** on the spot so that I can report my attendance.
12.	As a coach I want to **_update my profile info_** so that it is up-to-date.
13.	As a coach I want to **_register for a session_** so that KEEN SF knows that I am coming.
14.	As an athlete’s parent/guardian I want to **_login to KEEN MOBILE_** so that I can get access to required information and actions.
15.	As an athlete’s parent/guardian I want to **_notify KEENSF that my kid will be absent from a session_** so that KEENSF know about it.
16.	As an athlete’s parent/guardian I want to **_update my profile info*_* so that it is up-to-date.
17.	As a staff member I want to **_notify athletes and coaches about a new program_** so that they are informed and can express interest.
18.	As a staff member I want to **_notify athletes and coaches about a canceled program_** so that they know about it.
19.	As a staff member I want to **_notify athletes and coaches about an upcoming event_** so that they know about it and can express interest

**User Stories**

1) Login page.
1.1) User needs to login at the first page.
1.2) Keep login session until user logout.
2) List out available sessions.
2.1) Option to select: Coach option.
2.1.1) Option to select: Register Coach for available sessions.
2.1.2) Option to select: Register Athelets for available sessions.
2.1.3) Auto-Notification (1 day ahead).
2.1.3.1) send out SMS to Coach.
2.1.3.2) send out SMS to Atheletes.
2.1.3.3) notify User (no need SMS or email) -- just app notification.
2.2) Option to select: Session option.
2.2.1) Option to select: Send out Cancelling session.
2.2.2) Option to select: Check-in Coach for the session.
2.2.3) Option to select: Check-in Athletes for the session.
2.2.4) Option to select: Mark athletes as "Absent" for the session if miss the session.
2.2.5) Option to select: Send out new sessions available.
3) Data Warehouse.
3.1) Coach.
3.1.1) Update Coach profile.
3.1.2) List out all registered sessions belong to the Coach.
3.2) Atheletes.
3.2.1) Update Atheletes profile.
3.2.1) List out all registered sessions belong to the Atheletes.
          
