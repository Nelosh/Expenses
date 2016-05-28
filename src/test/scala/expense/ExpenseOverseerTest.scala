package expense

class ExpenseOverseerTest extends MySpec {
    val expenseOverseer = buildExpenseOverseer

    "ExpenseOverseer" should "return same number of persons who paid" in {
        expenseOverseer.getPersons should have size 3
    }

    it should "return persons with correct names and expenses" in {
        expenseOverseer.getPersons should contain (Person("Bob", 100))
        expenseOverseer.getPersons should contain (Person("Kate", 80))
        expenseOverseer.getPersons should contain (Person("Zack", 33))
    }

    it should "return correct total expense" in {
        expenseOverseer.getTotalExpense should === (213)
    }

    it should "return correct expense per person" in {
        expenseOverseer.getExpensePerPerson should === (71)
    }

    it should "return correct balances" in {
        expenseOverseer.getPersonBalances should contain (Balance("Bob", -29))
        expenseOverseer.getPersonBalances should contain (Balance("Kate", -9))
        expenseOverseer.getPersonBalances should contain (Balance("Zack", 38))
    }

    it should "return fair and correct transactions" in {
        expenseOverseer.getNecessaryTransactions should contain (Transaction("Zack", "Bob", 29))
        expenseOverseer.getNecessaryTransactions should contain (Transaction("Zack", "Kate", 9))
    }

    it should "return empty list of transaction with empty payments" in {
        val expenseOverseer = new ExpenseOverseer
        expenseOverseer.getNecessaryTransactions should === (List.empty)
    }

    def buildExpenseOverseer: ExpenseOverseer = new ExpenseOverseer()
      .add(Payment("Bob", 100))
      .add(Payment("Kate", 20))
      .add(Payment("Kate", 60))
      .add(Payment("Zack", 33))

}
