# iMali Circle

iMali Circle is an Android mobile application designed to help South African stokvel groups manage their savings activities in one place. The app allows members to manage contributions, view payout rotations, manage group members and keep track of important stokvel information.

The main idea behind iMali Circle is to make stokvel management easier and more organised instead of relying mainly on notebooks, WhatsApp messages and manual records.

## Why I Created iMali Circle

I created iMali Circle because stokvel groups can sometimes struggle with keeping accurate records of contributions and knowing who has paid or whose turn it is to receive a payout. This can lead to confusion and disagreements between members.

From my research into existing stokvel applications such as StokFella, Stoki and Mzansi Stokvel Co., I identified features that could be useful for my own application. I wanted iMali Circle to focus on offline contribution capture, local language support, simple payout tracking and allowing one member to capture a contribution on behalf of another member.

## Main Features
## Video Presentation

The video presentation demonstrates the main features and functionality of the iMali Circle Android application.

[Watch the iMali Circle Video Presentation](https://youtu.be/SW_WTCZ9fh8?si=PtqaAjmKzXmC5rAk)

### Account Registration and Login

Users can create an account using their:

- Full name
- Phone number
- Password

Passwords are hashed using bcrypt on the server instead of being stored as plain text. Users can log in using their phone number and password.

### Stokvel Management

Members can:

- Create a stokvel group
- Add members using their phone numbers
- Set the contribution amount
- Choose the contribution frequency
- Set the payout order
- View group members
- Assign member roles

The available roles are:

- Admin
- Treasurer
- Member

### Contribution Tracking

The application allows members to record contributions for each stokvel.

An admin or treasurer can also capture a contribution on behalf of another member. This is useful for members who may not have a smartphone or who need another member to record their contribution.

### Payout Rotation

iMali Circle provides a clear payout rotation so that members can see who is next to receive the payout.

The payout order is based on the rules set by the stokvel group.

### Offline Contribution Capture

One of the main features of iMali Circle is offline support.

A contribution can be recorded even when there is no internet connection. The contribution is first saved on the device using the local Room database and is marked as pending.

When the device gets an internet connection again, the application can synchronise the pending contribution with the server.

This helps prevent contribution records from being lost because of poor network connection.

### Multi-Language Support

The application is designed to support:

- English
- isiZulu
- isiXhosa

The language can be changed from the Settings section without needing to restart the application.

### Notifications

Firebase Cloud Messaging is planned for notifications such as:

- Contribution reminders
- Upcoming payouts
- Stokvel meetings
- Changes to meeting information
- Savings streak notifications

### Settings

The Settings section allows users to manage their preferences, including:

- Application language
- Push notifications
- Biometric login
- Phone number
- Password

## Design

The iMali Circle design uses a simple navy, green and gold colour palette.

The design was chosen to give the application a clear and consistent identity while keeping the screens simple and easy to use. The interface uses large and clear controls because the application is intended to be accessible to different types of stokvel members, including members who may not be very confident with technology.

The application also uses a circular icon concept. The overlapping dots represent the members of a stokvel and the circle represents the community and rotating nature of a stokvel.

## Technology Used

| Area | Technology |
|---|---|
| Mobile application | Kotlin |
| User Interface | Jetpack Compose |
| UI Design | Material 3 |
| Local Database | Room |
| Background Sync | WorkManager |
| Networking | Retrofit |
| HTTP Client | OkHttp |
| Backend | Node.js and Express |
| Database | PostgreSQL |
| Database Hosting | Railway |
| Notifications | Firebase Cloud Messaging |
| Authentication | JWT |
| Password Security | bcrypt |

## System Architecture

The application uses a client-server architecture.

The Android application communicates with the REST API. The REST API communicates with the PostgreSQL database. The Android application does not connect directly to the database.

The main structure is:

```text
Android Application
        |
        v
     REST API
        |
        v
   PostgreSQL
