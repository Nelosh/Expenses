package expense

class BalancedTest extends MySpec{
    val balance = Balance("Bob", 100)

    "Balanced" should "return correct Balance updatedBy value" in {
        balance.updatedBy(100) should === (Balance("Bob", 200))
        balance.updatedBy(-50) should === (Balance("Bob", 50))
    }

    it should "return correct Balance with updated value" in {
        balance.updated(107) should === (Balance("Bob", 107))
    }

}
