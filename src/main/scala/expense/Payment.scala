package expense

case class Payment(name: String, amount: Float)

case class Transaction(fromWho: String, toWhom: String, amount: Float)
