package textAlignment

enum class Alignment {
    LEFT,
    RIGHT,
    CENTER,
    JUSTIFY
}

class Text(_text: String = "", _width: Int = 120, _alignment: Alignment = Alignment.LEFT) {
    private var text: String = _text
    private var width = _width
    private var alignment: Alignment = _alignment

    operator fun plus(toAdd: String): Text = Text(this.text + toAdd, this.width, this.alignment)
    operator fun plus(toAdd: Text): Text = Text(this.text + toAdd.text, this.width, this.alignment)

    override fun toString(): String = text

    fun align(): String = when(alignment) {
        Alignment.LEFT -> alignLeft()
        Alignment.RIGHT -> ""
        Alignment.CENTER -> ""
        Alignment.JUSTIFY -> ""
    }

    private fun alignLeft(): String {
        var curPosition = 0
        var curWordLength = 0
        var curWord = ""
        var newText: String = "_".repeat(width) + "\n"
        for (letter in text) {
            when (letter) {
                '\n' -> {
                    newText += if (width - curPosition >= curWordLength) "$curWord\n" else "\n$curWord\n"
                    curPosition = 0
                    curWordLength = 0
                    curWord = ""
                }
                ' ' -> {
                    if (width - curPosition >= curWordLength) {
                        newText += curWord
                        curPosition += curWordLength + 1
                        if (width >= curPosition) {
                            newText += " "
                        }
                    } else {
                        if (width - curPosition > 0)
                            newText += " ".repeat(width - curPosition)
                        newText += "\n$curWord "
                        curPosition = curWordLength + 1
                    }
                    curWordLength = 0
                    curWord = ""
                }
                else -> {
                    curWordLength++
                    curWord += letter
                }
            }
        }
        newText += if (width - curPosition >= curWordLength) "$curWord " else " ".repeat(width - curPosition) + "\n$curWord "

        text = newText
        return text
    }
}