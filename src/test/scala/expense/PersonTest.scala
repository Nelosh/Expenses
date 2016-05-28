package expense

import app.expense.{Balance, Person}

class PersonTest extends MySpec {
    "A Person" should "have increased expenses after payment" in {
        val person = Person("Bob")
        person.paid(100) should === (Person("Bob", 100))
    }

    it should "not also forget about previous expenses after payment" in {
        val person = Person("Bob", 33)
        person.paid(133) should === (Person("Bob", 166))
    }

    it should "correctly convert to balance" in {
        val person = Person("Bob", 166)
        person.convertToBalance(100) should === (Balance("Bob", -66))
        person.convertToBalance(200) should === (Balance("Bob", 34))
    }

}
