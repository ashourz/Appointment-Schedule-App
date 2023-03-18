# My Schedule App
Personal appointment scheduling app for Android written in Kotlin

# Project Assignment
Create appointments with a date, time, location, and description. 
- The location should be a dropdown/select with the following options: San Diego, St. George, Park City, Dallas, Memphis, and Orlando
- Include a list of appointments
- Include ability to edit appointments
- Include ability to cancel (delete) an appointment
- Ensure the user interface to be simple yet elegant.

# Key Libraries
- Kotlin
  - Coroutines
  - Flow
- MVVM application structure
- Material Design 3
- Jetpack Compose with integrated MaterialDatePicker and MaterialTimePicker Views
- Jetpack Navigation
- Jetpack Room SQLite Database
- MaterialDatePicker and MaterialTimePicker API

# Key Features
- LazyList View of appointments displays all appointments in chronological order grouped by date.
- Date Sticky Headers allow for easy navigation of appointments in list
- Upon opening the app the appointment list will automatically  scroll to today's appointments
- Today button allows for quick return scroll to today's appointments
- Confirm Delete Appointment Popup prevents accidental appointment deletion by user.
- Light/Dark Theme Support

# Validation
- Data validation on Add and Update appointment operations ensures that every appointment submitted contains a valid Title, Date, Time, Duration greater than 0 minutes, and Location. (Appointment description is optional)
- Data validation on Update operations ensures redundant operations are not executed if no appointment values have been changed.
- Appointment overlap validation on Add and Update appointment operations ensures that no two appointments scheduled to the same location overlap in datetime scheduled. 
- Datetime scheduled is determined by date, time and duration values specified by the user.
- Error messages on Add and Update appointment screens highlight datafield in error as well as Error Message for user clarity.

# Unit and Integration Testing
- JUnit4
- Espresso
- Google Truth

# Known Issues 
- Snackbar messages either disappear too quickly or do not appear on database actions being taken (add, delete, update). This has been corrected in branch  **snackbar_message_update_03_18_23**

# Screenshots
<p float="left">
  <img src="https://user-images.githubusercontent.com/39238415/224545291-72c122fc-8037-4eec-a6cb-44b504a37e1b.png" width="160" /> 
  <img src="https://user-images.githubusercontent.com/39238415/224545274-62628dcd-0d67-4a5c-acd4-d544acec10f6.png" width="160" />
  <img src="https://user-images.githubusercontent.com/39238415/224545335-45f5d1a6-12ab-46ee-9a20-6178c832aa2a.png" width="160" />
  <img src="https://user-images.githubusercontent.com/39238415/224546016-92ff99c3-04a8-4d36-b87f-8ee14d800aaf.png" width="160" />
  <img src="https://user-images.githubusercontent.com/39238415/224545723-658b99f4-0c0d-48dc-998e-7ea3ba758b99.png" width="160" />
</p>
<p float="left">
  <img src="https://user-images.githubusercontent.com/39238415/224546770-b0a758c5-474b-4910-953c-58c48a8b36d8.png" width="160" />
  <img src="https://user-images.githubusercontent.com/39238415/224546369-fa78350d-51c6-4489-be83-ea71b97a781d.png" width="160" /> 
  <img src="https://user-images.githubusercontent.com/39238415/224545312-029a0c15-42bf-4cc6-818a-360ccebb5db2.png" width="160" />
  <img src="https://user-images.githubusercontent.com/39238415/224546801-f2d33e5d-dd71-4273-9599-774790e8ca0c.png" width="160" />
  <img src="https://user-images.githubusercontent.com/39238415/224545972-f8c16400-1f77-41ee-b0a9-43453f796935.png" width="160" />
</p>

**Screenshot appointment card data may not reflect actual validation standards enforced by the application
