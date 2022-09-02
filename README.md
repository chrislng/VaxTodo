# VaxTodo

Vaccination-scheduling GUI application prototype created with two teammates for a Software Engineering class.

![sped_up_VaxTodo_create_new_employee_and_login](https://user-images.githubusercontent.com/70879967/188214719-0bbc21f2-f0e4-42b2-9d38-9cfb23272dd1.gif)

The application UI is in French, while the code itself is in English.

The `\doc\` folder includes the JavaDoc, the Class, Activity and Sequence UML Diagrams done with Visual Paradigm inside the `\VPP\` folder, and the final report, translated to English, containing information about the Use Cases, the UML diagrams, some JUnit Unit Tests, and the JIRA Scrum board, along with other information.

## Features

- Book a vaccination period.
- Manage visitor, volunteer and employee accounts.
- Manage appointments.
- Add important vaccination-related information in a visitor's account and follow up on it.

### First Launch

To use the application, Java 17+ is required. If it is not installed one the user's computer, a pop-up message will appear informing the user about the issue and a browser page will open with the OpenJDK installation page.

![VaxTodo_JDK_warning](https://user-images.githubusercontent.com/70879967/188205600-adbb7eda-0942-4933-a30b-d77f6a5b3d71.gif)

On first launch, a folder called `VaxTodo_Folder` will be created containing the initial employee and volunteer test accounts. Additional employee, volunteer and visitor accounts will be stored in that folder, as well as appointments, forms and logs.

![VaxTodo_create_new_folder](https://user-images.githubusercontent.com/70879967/188205534-ac5ae988-a70a-47ef-9d87-922859e535e7.gif)

### Login

To initially log into the application, one of the following logins are used:

- Employee Role
  - username: `111111111` | password: `Password1!`
- Volunteer role
  - username: `111111111` | password: `Password1!`

These accounts are only meant for testing purposes and for creating the main accounts. A message will appear informing the user as such and advising to create a main account.

![VaxTodo_launch_login_test](https://user-images.githubusercontent.com/70879967/188205609-35506230-eb88-44bf-9c52-3158f07ac2a5.gif)

### Main Menu (Employee)

From the main menu, in the employee role, you can click on the option of your choice.

At any time you can click on the "back" button to go back or the "logout" button to return to the login screen.

- Visitor Management: Access the list of visitors and add, modify or delete a visitor.
- Interview / Form: Access the list of all visitor forms. It is possible to add, modify and delete them. The forms ask essential questions for each visitor.
![VaxTodo_form](https://user-images.githubusercontent.com/70879967/188205573-59fd033c-730d-42a8-a450-c6ac3302c830.gif)
- Consultation of planned visits: Access the list of planned visits and add, modify or delete a visit. It is also possible to send a reminder notification.
![VaxTodo_create_new_visit_and_send_reminder](https://user-images.githubusercontent.com/70879967/188205548-ba381d45-21f1-42c4-8581-5cc46a221c35.gif)
- Employee management: Access the list of employees and add, modify or delete an employee.
![sped_up_VaxTodo_create_new_employee_and_login](https://user-images.githubusercontent.com/70879967/188214719-0bbc21f2-f0e4-42b2-9d38-9cfb23272dd1.gif)
- Volunteer management: Access the list of volunteers and add, modify or delete a volunteer.

### Main Menu (Volunteer)

From the main menu, in the volunteer role, you can click on the option of your choice.

At any time you can click on the "back" button to go back or the "logout" button to return to the login screen.

- Visitor List Menu: Search and view the information of a visitor. It is not possible to make changes to this list.
- Viewing the list of visits: Access the list of planned visits, possibility to add, modify or delete a visit. It is also possible to send a reminder notification.

![VaxTodo_volunteer_menu_and_modify_visit](https://user-images.githubusercontent.com/70879967/188205616-7201fa76-9555-4d9d-ad5d-f4fa4e1fc83a.gif)

No management of employees or volunteers is possible while logged-in as a volunteer.

### Some additional information

- The application has several validations (input validation, read and write validation on files).
- Encryption of passwords and files with AES-256 encryption.
- Isolated code and use of inheritance and abstraction.
