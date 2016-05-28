package app.expense

import math._

class ExpenseOverseer {

    private var payments: List[Payment] = List()

    def add(payment: Payment): ExpenseOverseer = {
        payments ::= payment
        this
    }

    def getPayments: List[Payment] = payments

    def getNecessaryTransactions: List[Transaction] = {
        val balances = getPersonBalances
        val debtors = balances.filter(_.amount > 0)
        val creditors = balances.filter(_.amount < 0)
        formTransactions(List.empty)(debtors, creditors)
    }

    private def formTransactions(result: List[Transaction])(debtors: List[Balance], creditors: List[Balance]): List[Transaction] = {
        if (debtors.isEmpty || creditors.isEmpty) return result
        findDebtorForMaxCreditorStage(result)(debtors, creditors)
    }

    private def findDebtorForMaxCreditorStage(result: List[Transaction])(debtors: List[Balance], creditor: List[Balance]): List[Transaction] = {
        val maxCreditor = maxAbsoluteBalance(creditor)
        debtors.find(_.amount == abs(maxCreditor.amount)) match {
            case Some(debtor) =>
                val transaction = Transaction(debtor.name, maxCreditor.name, debtor.amount)
                formTransactions(transaction :: result)(debtors.filter(_ != debtor), creditor.filter(_ != maxCreditor))

            case None => findCreditorForMaxDebtorStage(result)(debtors, creditor)
        }
    }

    private def findCreditorForMaxDebtorStage(result: List[Transaction])(debtors: List[Balance], creditors: List[Balance]): List[Transaction] = {
        val maxDebtor = maxAbsoluteBalance(debtors)
        creditors.find(x => abs(x.amount) == maxDebtor.amount) match {
            case Some(creditor) =>
                val transaction = Transaction(maxDebtor.name, creditor.name, maxDebtor.amount)
                formTransactions(transaction :: result)(debtors.filter(_ != maxDebtor), creditors.filter(_ != creditor))

            case None =>
                maxAmountTransactionStage(result)(debtors, creditors)
        }
    }

    private def maxAmountTransactionStage(result: List[Transaction])(debtors: List[Balance], creditors: List[Balance]): List[Transaction] = {
        val maxCreditor = maxAbsoluteBalance(creditors)
        val maxDebtor = maxAbsoluteBalance(debtors)

        val maxPossibleTransactionAmount = min(maxDebtor.amount, abs(maxCreditor.amount))
        val transaction = Transaction(maxDebtor.name, maxCreditor.name, maxPossibleTransactionAmount)

        val updatedDebtors = balanceAfterTransaction(maxPossibleTransactionAmount)(maxDebtor, debtors)
        val updatedCreditors = balanceAfterTransaction(maxPossibleTransactionAmount)(maxCreditor, creditors)

        formTransactions(transaction :: result)(updatedDebtors, updatedCreditors)
    }

    private def balanceAfterTransaction(transactionAmount: Float)(affectedBalance: Balance, balances: List[Balance]): List[Balance] = {
        val sign = if(affectedBalance.amount < 0) -1 else 1
        if (abs(affectedBalance.amount) == transactionAmount)
            balances.filter(_ != affectedBalance)
        else
            affectedBalance.updatedBy(-sign*transactionAmount) :: balances.filter(_ != affectedBalance)
    }

    private def maxAbsoluteBalance(balances: List[Balance]): Balance = {
        balances.foldLeft(Balance(""))((left, right) => if (abs(left.amount) > abs(right.amount)) left else right)
    }

    def getTotalExpense: Float = payments.foldLeft(0f)((total, payment) => total + payment.amount)

    def getExpensePerPerson: Float = getTotalExpense / getPersons.size

    def getPersonBalances: List[Balance] = getPersons.map(_.convertToBalance(getExpensePerPerson))

    def getPersons: List[Person] = {
        def getPersonsHelper(persons: List[Person])(payments: List[Payment]): List[Person] = {
            if (payments.isEmpty)
                return persons

            val payment = payments.head
            val maybePerson = persons.find(payment.name == _.name)
            maybePerson match {
                case Some(person) =>
                    val updatedPerson = person.paid(payment.amount)
                    val personsExceptUpdated = persons.filter(person != _)
                    getPersonsHelper(updatedPerson :: personsExceptUpdated)(payments.tail)

                case None => getPersonsHelper(Person(payment.name, payment.amount) :: persons)(payments.tail)
            }
        }

        getPersonsHelper(List())(payments)
    }
}
