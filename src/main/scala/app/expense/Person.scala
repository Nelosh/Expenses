package app.expense

case class Person(name: String, expense: Float = 0) {
    def paid(amount: Float): Person = Person(name, expense + amount)
    def convertToBalance(optimalExpense: Float) = Balance(name, optimalExpense - expense)
}

case class Balance(name: String, amount: Float = 0) {
    def updatedBy(value: Float) = Balance(name, amount + value)
    def updated(value: Float) = Balance(name, value)
}
