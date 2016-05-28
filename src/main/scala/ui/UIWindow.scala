package ui

import java.awt.{Dimension, Font}

import expense.{Payment, ExpenseOverseer}

import scala.swing._

class UIWindow extends MainFrame {
    val standardFont = new Font("serif", Font.PLAIN, 50)

    val nameField = createTextField
    val serviceField = createTextField
    val amountField = createTextField

    val paymentsArea = createTextArea
    val expenseArea = createTextArea
    val transactionArea = createTextArea

    val totalLabel = new Label("0")
    val averageLabel = new Label("0")

    val expenseOverseer = new ExpenseOverseer

    preferredSize = new Dimension(2000, 1000)
    title = "Expense Overseer"
    contents = mainWindow
    normalizeFont(contents.head)

    private def buttonAdd = Button("Add") {
        addPaymentFromInput()
    }

    private def addPaymentFromInput(): Unit = {
        if (inputFieldsNonEmpty && amountIsNumber) {
            expenseOverseer.add(Payment(nameField.text, amountField.text.toInt))
            paymentsArea.append(nameField.text + " : " + serviceField.text + " - " + amountField.text + "$\n")
            refresh()
        }
    }

    private def refresh(): Unit = {
        totalLabel.text = expenseOverseer.getTotalExpense.toString
        averageLabel.text = expenseOverseer.getExpensePerPerson.toString
        expenseArea.text = expenseOverseer.getPersons.map(x => x.name + ": " + x.expense + "$").foldLeft("")((left, right) => left + right + "\n")
        transactionArea.text = expenseOverseer.getNecessaryTransactions.map(x => x.fromWho + " -> " + x.toWhom + ": " + x.amount + "$").foldLeft("")((left, right) => left + right + "\n")
    }

    private def amountIsNumber: Boolean = {
        try {
            amountField.text.toInt
        } catch {
            case e: NumberFormatException => return false
        }
        true
    }

    private def inputFieldsNonEmpty: Boolean = {
        nameField.text.nonEmpty && serviceField.text.nonEmpty && amountField.text.nonEmpty
    }

    private def createTextField: TextField = new TextField("") {
        maximumSize = new Dimension(Integer.MAX_VALUE, 80)
    }

    private def createTextArea: TextArea = new TextArea(""){
        editable = false
    }

    private def mainWindow: BoxPanel = {
        new BoxPanel(Orientation.Vertical) {
            contents += inputBox
            contents += buttonAddLine
            contents += Swing.VStrut(50)
            contents += outputBox
        }
    }

    private def inputBox: BoxPanel = {
        new BoxPanel(Orientation.Horizontal) {
            contents += nameInputBox
            contents += serviceInputBox
            contents += amountInputBox
        }
    }

    private def nameInputBox: BoxPanel = {
        new BoxPanel(Orientation.Vertical) {
            contents += new Label("Name")
            contents += nameField
        }
    }

    private def serviceInputBox: BoxPanel = {
        new BoxPanel(Orientation.Vertical) {
            contents += new Label("Service")
            contents += serviceField
        }
    }

    private def amountInputBox: BoxPanel = {
        new BoxPanel(Orientation.Vertical) {
            contents += new Label("Amount")
            contents += amountField
        }
    }

    private def buttonAddLine: BoxPanel = {
        new BoxPanel(Orientation.Horizontal) {
            contents += buttonAdd
            contents += Swing.HGlue
        }
    }

    private def outputBox: BoxPanel = {
        new BoxPanel(Orientation.Horizontal) {
            contents += paymentsArea
            contents += Swing.HStrut(30)
            contents += solutionOutputBox
        }
    }

    private def solutionOutputBox: BoxPanel = {
        new BoxPanel(Orientation.Vertical) {
            contents += totalOutputBox
            contents += averageOutputBox
            contents += expenseOutputBox
            contents += transactionOutputBox
        }
    }

    private def totalOutputBox: BoxPanel = {
        new BoxPanel(Orientation.Horizontal) {
            contents += new Label("Total: ")
            contents += totalLabel
            contents += Swing.HGlue
        }
    }

    private def averageOutputBox: BoxPanel = {
        new BoxPanel(Orientation.Horizontal) {
            contents += new Label("Average: ")
            contents += averageLabel
            contents += new Label(" to be paid by mate")
            contents += Swing.HGlue
        }
    }

    private def expenseOutputBox: BoxPanel = {
        new BoxPanel(Orientation.Horizontal) {
            contents += new BoxPanel(Orientation.Vertical) {
                contents += new Label("Expenses:")
                contents += expenseArea
            }
            contents += Swing.HGlue
        }
    }

    private def transactionOutputBox: BoxPanel = {
        new BoxPanel(Orientation.Horizontal) {
            contents += new BoxPanel(Orientation.Vertical) {
                contents += new Label("Transaction to be made:")
                contents += transactionArea
            }
            contents += Swing.HGlue
        }
    }

    private def normalizeFont(component: Component): Unit = {
        component match {
            case container: Container => container.contents.foreach(normalizeFont)
            case _ => component.font = standardFont
        }
    }

}
