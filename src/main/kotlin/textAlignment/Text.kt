package textAlignment

import javax.management.InvalidAttributeValueException


class Text(private val sourceText: String = "", private var width: Int = 120) {
    /*
    *   It's decided to keep the original text separate from the aligned one.
    *   This is because there is no rule to distinguish between user-supplied
    *   line breaks and line breaks that appear when aligned.
    *   After all, when re-aligning the text, user's line breaks cannot be removed, but line breaks that were added during alignment, must be removed before re-alignment.
    *   Therefore, when aligning, the original text is taken.
    */
    private var alignedText = ""

    init {
        if (width < 1)
            throw InvalidAttributeValueException("Width of the text must be positive")
    }

    operator fun plus(toAdd: String): Text = Text(sourceText + toAdd, width)
    operator fun plus(toAdd: Text): Text = Text(sourceText + toAdd.sourceText, width)

    override fun toString(): String {
        return alignedText.ifEmpty { sourceText }
    }

    // Left alignment
    fun alignLeft(width: Int = this.width): String {
        /*
        *   Algorithm loop through the text, takes word by word and process them according to the next rules:
        *     - If word fits in the current line, then add it to the current line
        *     - If word does not fit in the current line, then add word to the new line
        *     - If word's length is bigger than width, then fill the rest of the current line with
        *           part of the word and split the rest of the word along the following lines
        */
        if (width < 1)
            throw InvalidAttributeValueException("Width of the text must be positive")

        this.width = width // In case when passed width is not equal to current width. So current width is updated to passed one
        var curPosition = 0 // Current position of the current line
        var curWord = "" // Current word
        var newText = ""
        for (letter in sourceText) {
            when (letter) {
                '\n' -> {
                    newText += if (width - curPosition >= curWord.length) "$curWord\n" else "\n$curWord\n"
                    curPosition = 0 // New line
                    curWord = "" // Reset current word
                }
                ' ' -> {
                    if (curWord.length > width) { // If word's length is bigger than line width then split the word
                        if (width < curPosition) { // Go to next line if current line is fully filled
                            newText += "\n"
                            curPosition = 0
                        }
                        newText += curWord.substring(0, width - curPosition) + "\n" // Fill the rest of the current line with the part of the word
                        curWord = curWord.drop(width - curPosition) // Delete that part
                        while (curWord.length >= width) { // While the rest of the word is bigger than line width
                            newText += curWord.substring(0, width) + "\n" // Fill the line with the part of the word and go to next line
                            curWord = curWord.drop(width) // Delete that part
                        }
                        newText += curWord // Add the last part of the word to current line
                        curPosition += curWord.length // Change current position
                        curWord = "" // Reset current word
                    }
                    if (width - curPosition >= curWord.length) { // If there is enough place to put the word
                        newText += curWord // Add word to the current line
                        curPosition += curWord.length + 1 // Change current position (including space)
                        if (width >= curPosition) { // If it's not EOL, add space after last word
                            newText += " "
                        }
                    } else { // If there is not enough place to put the word
                        newText += if (curWord.length == width) "\n$curWord" else "\n$curWord " // Add new word
                        curPosition = curWord.length + 1 // Change position
                    }
                    curWord = "" // Reset current word
                }
                else -> { // Get another letter of the word
                    curWord += letter
                }
            }
        }

        // Process the last word (same as other words)
        if (curWord.length > width) { // If word's length is bigger than line width then split the word
            if (width < curPosition) {
                newText += "\n"
                curPosition = 0
            }
            newText += curWord.substring(0, width - curPosition) + "\n"
            curWord = curWord.drop(width - curPosition)
            while (curWord.length >= width) {
                newText += curWord.substring(0, width) + "\n"
                curWord = curWord.drop(width)
            }
            newText += curWord
        }
        else if (width - curPosition >= curWord.length) { // If there is enough place to put the word
            newText += curWord
            curPosition += curWord.length + 1
        }
        else { // If there is not enough place to put the word
            newText += "\n$curWord"
        }

        alignedText = newText // Replace old text with aligned one
        return alignedText
    }
}