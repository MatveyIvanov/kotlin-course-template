package textAlignmentTests

import javax.management.InvalidAttributeValueException
import textAlignment.Text
import org.junit.Assert
import org.junit.Test


const val TEXT1 = "Sometimes you still make small grammar mistakes, and I think you can improve your vocabulary. I recommend you review many of the language points we studied this term."
const val TEXT2 = "Параллелепипед - это геометрическая фигура"

class TextTest {

    @Test
    fun test_alignment_with_default_width() {
        val text = Text(TEXT1)
        Assert.assertEquals(
            "Sometimes you still make small grammar mistakes, and I think you can improve your vocabulary. I recommend you review \n" +
                    "many of the language points we studied this term.", text.alignLeft()
        )
    }

    @Test
    fun test_alignment_with_custom_width() {
        val text = Text(TEXT1, 55)
        Assert.assertEquals("Sometimes you still make small grammar mistakes, and I \n" +
                "think you can improve your vocabulary. I recommend you \n" +
                "review many of the language points we studied this \n" +
                "term.", text.alignLeft())
    }

    @Test
    fun test_alignment_keeping_punctuation_mark_with_word() {
        // This test checks if punctuation mark is tied to the word when word right at the end of the line
        val text = Text(TEXT1, 47)
        /* There is no enough space for 'mistakes' just because of comma,
           but it still moves to the next line with this comma
         */
        Assert.assertEquals("Sometimes you still make small grammar \n" +
                "mistakes, and I think you can improve your \n" +
                "vocabulary. I recommend you review many of the \n" +
                "language points we studied this term.", text.alignLeft())
        /* There is enough space for 'mistakes' + comma,
           so it stays at "current" line
         */
        Assert.assertEquals("Sometimes you still make small grammar mistakes,\n" +
                "and I think you can improve your vocabulary. I \n" +
                "recommend you review many of the language points\n" +
                "we studied this term.", text.alignLeft(48))
    }

    @Test
    fun test_alignment_with_custom_width_last_word_perfectly_fits() {
        val modifiedText = TEXT1.dropLast(1) // Remove last dot so 'term' will not be moved on a new line
        val text = Text(modifiedText, 55)

        Assert.assertEquals("Sometimes you still make small grammar mistakes, and I \n" +
                "think you can improve your vocabulary. I recommend you \n" +
                "review many of the language points we studied this term", text.alignLeft())
    }

    @Test
    fun test_alignment_with_word_splitting_two_lines() {
        val text = Text(TEXT2, 10)
        Assert.assertEquals(
            "Параллелеп\n" +
                    "ипед - это\n" +
                    "геометриче\n" +
                    "ская \n" +
                    "фигура", text.alignLeft()
        )
    }

    @Test
    fun test_alignment_with_word_splitting_multiple_lines() {
        val text = Text(TEXT2, 5)
        Assert.assertEquals(
            "Парал\n" +
                    "лелеп\n" +
                    "ипед \n" +
                    "- это\n" +
                    "геоме\n" +
                    "триче\n" +
                    "ская \n" +
                    "фигур\n" +
                    "а", text.alignLeft()
        )
    }

    @Test
    fun test_exception_wrong_width_when_initializing() {
        try {
            Text(TEXT1, -1)
        } catch (e: InvalidAttributeValueException) {
            Assert.assertEquals("Width of the text must be positive", e.message)
        }
    }

    @Test
    fun test_exception_wrong_width_when_aligning() {
        try {
            Text(TEXT1).alignLeft(0)
        } catch (e: InvalidAttributeValueException) {
            Assert.assertEquals("Width of the text must be positive", e.message)
        }
    }
}