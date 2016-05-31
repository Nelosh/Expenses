#Application description#

##User help##

This applications main purpose is calculating necessary transactions between friends to equalize expenses on the trip.

Application presents 3 fields for inserting data.
Text field for inserting friends name (under label "Name")
Text field for inserting servise name friend pay for (under label "Service")
Text field for inserting amount spent on the service by th friend (under label "Amount")

For adding inserted data there is button "Add". Clicking this button add data to system, which then used to recalculate result.
If data will be inserted incorrect (e.g. One or more text fields are left empty or amount is not a number) data will system won't show any reaction, and data won't be added.

After data was inserted there are 4 helpfull areas for representing result of the calculation.
Left area records inserted data for easier tracking.
Right-top show total expenses of the group and expenses considered for each person.
Right-middle represents summary of the expenses made for every person.
Right-bottom show transations necessary to be made (wich is the solution required by the main algorythm)

Also, application have helpful button for changing font size, made purely for conveniece adjusting visibility.
Clicking this button, shows additional window for inserting new size of the font (new value must be a number, any float value will be rounded)

##Testing##

Application code contains test for testing main method of the business logic.

Most important class tested is ExpenseOverseer wich contains methods for calculating Transaction depenig from the payments made.
Test create simple example of the insert data and check the result of the ExpenseOverseer methods with the expected ones.

Also the are test for checkink correct work of the additional Data Structures as Person and Balance, which contain method made purely for this Data Structures convenient use. 
Data Structures Payments and Transactions was not tested as they do not contain any methods at all.

UIWindow was tested by hand through repetetive program runs, as this class basicly replaces Main class and do not public methods with pure functional calcultions, but directed on the User Interface only.
