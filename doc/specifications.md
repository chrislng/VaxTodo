# Specifications

These are the specifications, translated from French, that were given by the professor for a fictional client company.

## Constraints

- Calendar: No more than 15 entries/visits per time slot (8am, 9am, 10am...5pm).
- Schedule: Monday to Friday from 8am to 6pm.
- Appointments: Minimum 72 hours in advance and maximum 2 people.

## Visit

When a person arrives at the reception desk, a volunteer checks to see if he or she has an appointment, using his or her reservation number and validating his or her name and appointment time in the system. If the person does not have an appointment or is late, the volunteer checks the calendar for available times. The volunteer confirms the period with the visitor and proceeds to add the visitor to the calendar at the selected period by indicating his name and phone number. If a person who has made an appointment arrives at the reception desk more than 15 minutes late, the appointment is automatically cancelled by the system and the person is considered a spontaneous visitor. The volunteer then proceeds to select an available period.

When making the appointment, the employee uses the system to access the calendar to choose a date and time with the person. When the person confirms the choice to the employee, the employee makes the reservation and adds a scheduled visit to the calendar. The appointment details are emailed to the visitor, who can use them during their visit to confirm their appointment.

- Reservation number: 6 digits
- First name: 50 character limit
- Last name: 50 character limit
- Date of visit Accepted format: YYYY-MM-DD
- Time of visit Accepted format: HH:mm
- Dose Type: (1, 2)

## Interview and Follow-Up

During the interview with the employee, in the case of a scheduled visit, the employee asks for his reservation number and confirms his visit in the system. In the case of a spontaneous visit, the employee verifies that his name and phone number are present in the calendar at the time of the visit. The employee then asks for his or her account number to proceed with the questionnaire. If the visitor does not have his account number, he can provide his email address and date of birth to find it.

The employee asks for the visitor's health insurance card and completes a questionnaire with the visitor in the system. If the visitor does not have a health card or if it has expired, the visit is cancelled and the employee invites the visitor to make a new appointment.

In the case of a first dose, the employee asks the visitor if they would like to schedule a second dose. If so, the employee proceeds to schedule the appointment. The appointment must be made within a minimum of one month.

The employee then prints and signs the form to be completed by a health care professional. During the day, an employee collects the completed forms and completes the visitor's vaccination profile. If the visitor has been vaccinated, an email is sent to the visitor containing the proof of vaccination in the form of a PDF document. The proof of vaccination contains the visitor's name, date of birth, a QR code and their vaccination profile listing the vaccinations given. At the end of the day, the employee retrieves the list of scheduled visits for the next 48 hours and sends an email notification, reminding everyone of their upcoming appointment.

## Form

- Account number: 12 digits
- First name: 50 character limit
- Last name: 50 character limit
- Date of birth Accepted format: YYYY-MM-DD
- Health insurance card number: Entered without spaces
- Date of visit Accepted format: YYYY-MM-DD
- Have you ever received a first dose? Accepted answer: Yes, No
- Have you ever had COVID? Answer accepted: Yes, No
- Do you have any symptoms of COVID? Answer accepted: Yes, No
- Do you have any allergies? Answer accepted: Yes, No
- Which vaccine would you like to receive? Choice: Moderna, Pfizer, AstraZeneca, Janssen
- Have you had the vaccine? Answer accepted: Yes, No
- Vaccine name Choice: Moderna, Pfizer, AstraZeneca, Janssen
- Vaccine code: 24 character limit

## User account

All volunteers and employees have a user account, which is used to log in to the application.

- Identification code: 9 digits
- Password: Consists of at least 8 characters containing at least 1 number, 1 upper case, 1 lower case and 1 special character.

## Visitor

During a first visit, the employee must create an account for the visitor that will be linked to all his activities with VaxTodo. Once created, the account number is communicated to the visitor and a confirmation email is sent with the account number.

- Account number: 12 characters
- First name: 50 character limit
- Last name: 50 character limit
- Date of birth Accepted format: YYYY-MM-DD
- Email Address
- Phone number: 10 digits
- Address (number, street): 100 character limit
- Postal Code: 6 characters
- City: 50 characters limit

### Vaccination profile

List of vaccinations received

- Date of vaccination Accepted format: YYYY-MM-DD
- Dose type: (1, 2)
- Vaccine name: [Moderna, Pfizer, AstraZeneca, Janssen]
- Vaccine code: 24 character limit

## Volunteer

- Account number: 12 characters
- First name: 50 character limit
- Last name: 50 character limit
- Date of birth Accepted format: YYYY-MM-DD
- Email Address
- Phone number: 10 digits
- Address (number, street): 100 character limit
- Postal Code: 6 characters
- City: 50 character limit

A volunteer also has a schedule listing the days he or she is available to come to the location.
