package app.ui

import java.awt.{Dimension, Font}

import app.expense.{ExpenseOverseer, Payment}

import scala.swing._

class UIWindow extends MainFrame {
    var fontSize = 50

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

    def standardFont = new Font("serif", Font.PLAIN, fontSize)

    private def changeFontButton = Button("Change Font") {
        changeFont()
    }

    def changeFont() {
        val result = Dialog.showInput(contents.head, "New Font", initial=fontSize.toString)
        result match {
            case Some(size) =>
                fontSize = size.toInt
                normalizeFont(contents.head)
            case None =>
        }
    }

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
        expenseArea.text = formattedPersons
        transactionArea.text = formattedTransactions
    }

    private def formattedPersons: String = {
        expenseOverseer.getPersons.map(x => x.name + ": " + x.expense + "$").mkString("\n")
    }

    private def formattedTransactions: String = {
        expenseOverseer.getNecessaryTransactions.map(x => x.fromWho + " -> " + x.toWhom + ": " + x.amount + "$").mkString("\n")
    }

    private def amountIsNumber: Boolean = {
        try {
            amountField.text.toFloat
        } catch {
            case e: NumberFormatException => return false
        }
        true
    }

    private def inputFieldsNonEmpty: Boolean = {
        nameField.text.nonEmpty && serviceField.text.nonEmpty && amountField.text.nonEmpty
    }

    private def createTextField: TextField = new TextField("") {
        maximumSize = new Dimension(Integer.MAX_VALUE, fontSize * 3 / 2)
    }

    private def createTextArea: TextArea = new TextArea("") {
        editable = false
    }

    private def mainWindow: BoxPanel = {
        new BoxPanel(Orientation.Vertical) {
            contents += inputBox
            contents += buttonAddLine
            contents += Swing.VStrut(10)
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
            contents += changeFontButton
        }
    }

    private def outputBox: BoxPanel = {
        new BoxPanel(Orientation.Horizontal) {
            contents += new ScrollPane(paymentsArea) {
                horizontalScrollBarPolicy = ScrollPane.BarPolicy.Never
            }
            contents += Swing.HStrut(15)
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
        new BoxPanel(Orientation.Vertical) {
            contents += new BoxPanel(Orientation.Horizontal) {
                contents += new Label("Expenses:")
                contents += Swing.HGlue
            }
            contents += new ScrollPane(expenseArea) {
                horizontalScrollBarPolicy = ScrollPane.BarPolicy.Never
            }
        }
    }

    private def transactionOutputBox: BoxPanel = {
        new BoxPanel(Orientation.Vertical) {
            contents += new BoxPanel(Orientation.Horizontal) {
                contents += new Label("Transaction to be made:")
                contents += Swing.HGlue
            }
            contents += new ScrollPane(transactionArea) {
                horizontalScrollBarPolicy = ScrollPane.BarPolicy.Never
            }
        }

    }

    private def normalizeFont(component: Component): Unit = {
        component match {
            case container: Container => container.contents.foreach(normalizeFont)
            case _ => component.font = standardFont
        }
    }

}
