@file:Suppress("UNUSED_PARAMETER")

package lesson9.task2

import lesson9.task1.Cell
import lesson9.task1.Matrix
import lesson9.task1.createMatrix
import kotlin.math.abs
import kotlin.math.min

// Все задачи в этом файле требуют наличия реализации интерфейса "Матрица" в Matrix.kt

/**
 * Пример
 *
 * Транспонировать заданную матрицу matrix.
 * При транспонировании строки матрицы становятся столбцами и наоборот:
 *
 * 1 2 3      1 4 6 3
 * 4 5 6  ==> 2 5 5 2
 * 6 5 4      3 6 4 1
 * 3 2 1
 */
fun <E> transpose(matrix: Matrix<E>): Matrix<E> {
    if (matrix.width < 1 || matrix.height < 1) return matrix
    val result = createMatrix(height = matrix.width, width = matrix.height, e = matrix[0, 0])
    for (i in 0 until matrix.width) {
        for (j in 0 until matrix.height) {
            result[i, j] = matrix[j, i]
        }
    }
    return result
}

fun <E> mirrorVertical(matrix: Matrix<E>): Matrix<E> {
    if (matrix.width < 1 || matrix.height < 1) return matrix
    val result = createMatrix(height = matrix.width, width = matrix.height, e = matrix[0, 0])
    for (i in 0 until matrix.width) {
        for (j in 0 until matrix.height) {
            result[i, j] = matrix[i, matrix.width - 1 - j]
        }
    }
    return result
}

/**
 * Пример
 *
 * Сложить две заданные матрицы друг с другом.
 * Складывать можно только матрицы совпадающего размера -- в противном случае бросить IllegalArgumentException.
 * При сложении попарно складываются соответствующие элементы матриц
 */
operator fun Matrix<Int>.plus(other: Matrix<Int>): Matrix<Int> {
    require(!(width != other.width || height != other.height))
    if (width < 1 || height < 1) return this
    val result = createMatrix(height, width, this[0, 0])
    for (i in 0 until height) {
        for (j in 0 until width) {
            result[i, j] = this[i, j] + other[i, j]
        }
    }
    return result
}

fun main() {
    println(generateSpiral(7, 4))
}

/**
 * Сложная (5 баллов)
 *
 * Заполнить матрицу заданной высоты height и ширины width
 * натуральными числами от 1 до m*n по спирали,
 * начинающейся в левом верхнем углу и закрученной по часовой стрелке.
 *
 * Пример для height = 3, width = 4:
 *  1  2  3  4
 * 10 11 12  5
 *  9  8  7  6
 */
fun generateSpiral(height: Int, width: Int): Matrix<Int> {
    val matrix = createMatrix(height, width, 1)
    var rowStart = 0
    var rowFinish = height - 1
    var columnStart = 0
    var columnFinish = width - 1
    var inc = 1
    while (inc <= height * width) {
        for (c in columnStart..columnFinish) matrix[Cell(rowStart, c)] = inc++
        rowStart++
        if (inc <= height * width) for (r in rowStart..rowFinish) matrix[Cell(r, columnFinish)] = inc++
        columnFinish--
        if (inc <= height * width) for (c in columnFinish downTo columnStart) matrix[Cell(rowFinish, c)] = inc++
        rowFinish--
        if (inc <= height * width) for (r in rowFinish downTo rowStart) matrix[Cell(r, columnStart)] = inc++
        columnStart++
    }
    return matrix
}

/**
 * Сложная (5 баллов)
 *
 * Заполнить матрицу заданной высоты height и ширины width следующим образом.
 * Элементам, находящимся на периферии (по периметру матрицы), присвоить значение 1;
 * периметру оставшейся подматрицы – значение 2 и так далее до заполнения всей матрицы.
 *
 * Пример для height = 5, width = 6:
 *  1  1  1  1  1  1
 *  1  2  2  2  2  1
 *  1  2  3  3  2  1
 *  1  2  2  2  2  1
 *  1  1  1  1  1  1
 */
fun generateRectangles(height: Int, width: Int): Matrix<Int> {
    val matrix = createMatrix(height, width, 1)
    var rowStart = 0
    var rowFinish = height - 1
    var columnStart = 0
    var columnFinish = width - 1
    var inc = 1
    while (rowStart <= rowFinish && columnStart <= columnFinish) {
        for (r in rowStart..rowFinish) {
            matrix[Cell(r, columnStart)] = inc
            matrix[Cell(r, columnFinish)] = inc
        }
        for (c in columnStart..columnFinish) {
            matrix[Cell(rowStart, c)] = inc
            matrix[Cell(rowFinish, c)] = inc
        }
        rowStart++
        rowFinish--
        columnStart++
        columnFinish--
        inc++
    }
    return matrix
}

/**
 * Сложная (5 баллов)
 *
 * Заполнить матрицу заданной высоты height и ширины width диагональной змейкой:
 * в левый верхний угол 1, во вторую от угла диагональ 2 и 3 сверху вниз, в третью 4-6 сверху вниз и так далее.
 *
 * Пример для height = 5, width = 4:
 *  1  2  4  7
 *  3  5  8 11
 *  6  9 12 15
 * 10 13 16 18
 * 14 17 19 20
 */
fun generateSnake(height: Int, width: Int): Matrix<Int> {
    val matrix = createMatrix(height, width, 0)
    var rowStart = 0
    var columnStart = 0
    var inc = 1
    while (rowStart < height && columnStart < width) {
        //h++ r--
        for (i in 0..min(columnStart, height - 1 - rowStart)) {
            matrix[Cell(rowStart + i, columnStart - i)] = inc++
        }
        if (columnStart < width - 1) columnStart++ else rowStart++
    }
    return matrix
}

/**
 * Средняя (3 балла)
 *
 * Содержимое квадратной матрицы matrix (с произвольным содержимым) повернуть на 90 градусов по часовой стрелке.
 * Если height != width, бросить IllegalArgumentException.
 *
 * Пример:    Станет:
 * 1 2 3      7 4 1
 * 4 5 6      8 5 2
 * 7 8 9      9 6 3
 */
fun <E> rotate(matrix: Matrix<E>): Matrix<E> = if (matrix.height != matrix.width) throw IllegalArgumentException() else
    mirrorVertical(transpose(matrix))

/**
 * Сложная (5 баллов)
 *
 * Проверить, является ли квадратная целочисленная матрица matrix латинским квадратом.
 * Латинским квадратом называется матрица размером n x n,
 * каждая строка и каждый столбец которой содержат все числа от 1 до n.
 * Если height != width, вернуть false.
 *
 * Пример латинского квадрата 3х3:
 * 2 3 1
 * 1 2 3
 * 3 1 2
 */
fun isLatinSquare(matrix: Matrix<Int>): Boolean =
    if (matrix.height != matrix.width) false else (1..matrix.height).toSet().let { set ->
        val n = matrix.height
        val mutableSet = mutableSetOf<Int>()
        val mutableSet2 = mutableSetOf<Int>()
        for (i in 0 until n) {
            mutableSet.clear()
            mutableSet2.clear()
            for (j in 0 until n) {
                mutableSet.add(matrix[Cell(i, j)])
                mutableSet2.add(matrix[Cell(j, i)])
            }
            if (mutableSet != set || mutableSet2 != set) return@let false
        }
        true
    }

/**
 * Средняя (3 балла)
 *
 * В матрице matrix каждый элемент заменить суммой непосредственно примыкающих к нему
 * элементов по вертикали, горизонтали и диагоналям.
 *
 * Пример для матрицы 4 x 3: (11=2+4+5, 19=1+3+4+5+6, ...)
 * 1 2 3       11 19 13
 * 4 5 6  ===> 19 31 19
 * 6 5 4       19 31 19
 * 3 2 1       13 19 11
 *
 * Поскольку в матрице 1 х 1 примыкающие элементы отсутствуют,
 * для неё следует вернуть как результат нулевую матрицу:
 *
 * 42 ===> 0
 */
fun sumNeighbours(matrix: Matrix<Int>): Matrix<Int> = createMatrix(matrix.height, matrix.width, 0).let { newMatrix ->
    fun getSafe(i: Int, j: Int): Int = if (i in 0 until matrix.height && j in 0 until matrix.width) matrix[i, j] else 0
    fun getSum(iIndex: Int, jIndex: Int): Int = (jIndex - 1..jIndex + 1).let { jList ->
        (iIndex - 1..iIndex + 1).sumOf { i -> jList.sumOf { j -> getSafe(i, j) } } - matrix[iIndex, jIndex]
    }
    for (i in 0 until newMatrix.height) {
        for (j in 0 until newMatrix.width) {
            newMatrix[i, j] = getSum(i, j)
        }
    }
    newMatrix
}

/**
 * Средняя (4 балла)
 *
 * Целочисленная матрица matrix состоит из "дырок" (на их месте стоит 0) и "кирпичей" (на их месте стоит 1).
 * Найти в этой матрице все ряды и колонки, целиком состоящие из "дырок".
 * Результат вернуть в виде Holes(rows = список дырчатых рядов, columns = список дырчатых колонок).
 * Ряды и колонки нумеруются с нуля. Любой из спискоов rows / columns может оказаться пустым.
 *
 * Пример для матрицы 5 х 4:
 * 1 0 1 0
 * 0 0 1 0
 * 1 0 0 0 ==> результат: Holes(rows = listOf(4), columns = listOf(1, 3)): 4-й ряд, 1-я и 3-я колонки
 * 0 0 1 0
 * 0 0 0 0
 */
fun findHoles(matrix: Matrix<Int>): Holes {
    val rowsMass = MutableList(matrix.height) { 0 }
    val columnMass = MutableList(matrix.width) { 0 }
    for (i in 0 until matrix.height) {
        for (j in 0 until matrix.width) {
            if (matrix[i, j] == 0) {
                rowsMass[i] = rowsMass[i] + 1
                columnMass[j] = columnMass[j] + 1
            }
        }
    }
    return Holes(
        (0 until matrix.height).filter { rowsMass[it] == matrix.width },
        (0 until matrix.width).filter { columnMass[it] == matrix.height }
    )
}

/**
 * Класс для описания местонахождения "дырок" в матрице
 */
data class Holes(val rows: List<Int>, val columns: List<Int>)

/**
 * Средняя (3 балла)
 *
 * В целочисленной матрице matrix каждый элемент заменить суммой элементов подматрицы,
 * расположенной в левом верхнем углу матрицы matrix и ограниченной справа-снизу данным элементом.
 *
 * Пример для матрицы 3 х 3:
 *
 * 1  2  3      1  3  6
 * 4  5  6  =>  5 12 21
 * 7  8  9     12 27 45
 *
 * К примеру, центральный элемент 12 = 1 + 2 + 4 + 5, элемент в левом нижнем углу 12 = 1 + 4 + 7 и так далее.
 */
fun sumSubMatrix(matrix: Matrix<Int>): Matrix<Int> {
    val newMatrix = createMatrix(matrix.height, matrix.width, 0)
    fun getSafe(i: Int, j: Int): Int =
        if (i in 0 until matrix.height && j in 0 until matrix.width) newMatrix[i, j] else 0
    for (i in 0 until matrix.height) {
        for (j in 0 until matrix.width) {
            newMatrix[i, j] = matrix[i, j] + getSafe(i - 1, j) + getSafe(i, j - 1) - getSafe(i - 1, j - 1)
        }
    }
    return newMatrix
}

/**
 * Простая (2 балла)
 *
 * Инвертировать заданную матрицу.
 * При инвертировании знак каждого элемента матрицы следует заменить на обратный
 */
operator fun Matrix<Int>.unaryMinus(): Matrix<Int> = this.let { matrix ->
    for (i in 0 until matrix.height) {
        for (j in 0 until matrix.width) {
            matrix[i, j] = -matrix[i, j]
        }
    }
    matrix
}

/**
 * Средняя (4 балла)
 *
 * Перемножить две заданные матрицы друг с другом.
 * Матрицы можно умножать, только если ширина первой матрицы совпадает с высотой второй матрицы.
 * В противном случае бросить IllegalArgumentException.
 * Подробно про порядок умножения см. статью Википедии "Умножение матриц".
 */
operator fun Matrix<Int>.times(other: Matrix<Int>): Matrix<Int> =
    createMatrix(this.height, other.width, 0).let { matrix ->
        for (i in 0 until matrix.height) {
            for (j in 0 until matrix.width) {
                matrix[i, j] =
                    (0 until this.width).sumOf { this[i, it] * other[it, j] }
            }
        }
        matrix
    }

/**
 * Сложная (7 баллов)
 *
 * Даны мозаичные изображения замочной скважины и ключа. Пройдет ли ключ в скважину?
 * То есть даны две матрицы key и lock, key.height <= lock.height, key.width <= lock.width, состоящие из нулей и единиц.
 *
 * Проверить, можно ли наложить матрицу key на матрицу lock (без поворота, разрешается только сдвиг) так,
 * чтобы каждой единице в матрице lock (штырь) соответствовал ноль в матрице key (прорезь),
 * а каждому нулю в матрице lock (дырка) соответствовала, наоборот, единица в матрице key (штырь).
 * Ключ при сдвиге не может выходить за пределы замка.
 *
 * Пример: ключ подойдёт, если его сдвинуть на 1 по ширине
 * lock    key
 * 1 0 1   1 0
 * 0 1 0   0 1
 * 1 1 1
 *
 * Вернуть тройку (Triple) -- (да/нет, требуемый сдвиг по высоте, требуемый сдвиг по ширине).
 * Если наложение невозможно, то первый элемент тройки "нет" и сдвиги могут быть любыми.
 */
fun canOpenLock(key: Matrix<Int>, lock: Matrix<Int>): Triple<Boolean, Int, Int> {
    fun check(iDelta: Int, jDelta: Int): Boolean {
        for (i in 0 until key.height) {
            for (j in 0 until key.width) {
                if (key[i, j] == lock[i + iDelta, j + jDelta]) return false
            }
        }
        return true
    }
    for (i in 0..lock.height - key.height) {
        for (j in 0..lock.width - key.width) {
            if (check(i, j)) return Triple(true, i, j)
        }
    }
    return Triple(false, 0, 0)
}

/**
 * Сложная (8 баллов)
 *
 * В матрице matrix размером 4х4 дана исходная позиция для игры в 15, например
 *  5  7  9  1
 *  2 12 14 15
 *  3  4  6  8
 * 10 11 13  0
 *
 * Здесь 0 обозначает пустую клетку, а 1-15 – фишки с соответствующими номерами.
 * Напомним, что "игра в 15" имеет квадратное поле 4х4, по которому двигается 15 фишек,
 * одна клетка всегда остаётся пустой. Цель игры -- упорядочить фишки на игровом поле.
 *
 * В списке moves задана последовательность ходов, например [8, 6, 13, 11, 10, 3].
 * Ход задаётся номером фишки, которая передвигается на пустое место (то есть, меняется местами с нулём).
 * Фишка должна примыкать к пустому месту по горизонтали или вертикали, иначе ход не будет возможным.
 * Все номера должны быть в пределах от 1 до 15.
 * Определить финальную позицию после выполнения всех ходов и вернуть её.
 * Если какой-либо ход является невозможным или список содержит неверные номера,
 * бросить IllegalStateException.
 *
 * В данном случае должно получиться
 * 5  7  9  1
 * 2 12 14 15
 * 0  4 13  6
 * 3 10 11  8
 */
fun fifteenGameMoves(matrix: Matrix<Int>, moves: List<Int>): Matrix<Int> {
    fun unreal(cell1: Cell, cell2: Cell?): Boolean =
        cell2 == null || abs(cell1.column - cell2.column) + abs(cell1.row - cell2.row) != 1

    val map = mutableMapOf<Int, Cell>()
    for (i in 0 until matrix.height) {
        for (j in 0 until matrix.width) {
            map[matrix[i, j]] = Cell(i, j)
        }
    }
    for (move in moves) {
        if (unreal(map[0]!!, map[move])) throw IllegalStateException()
        val cellTemp = map[0]!!
        map[0] = map[move]!!
        map[move] = cellTemp
    }
    val newMatrix = createMatrix(matrix.height, matrix.width, 0)
    for ((key, value) in map) {
        newMatrix[value] = key
    }
    return newMatrix
}

/**
 * Очень сложная (32 балла)
 *
 * В матрице matrix размером 4х4 дана исходная позиция для игры в 15, например
 *  5  7  9  2
 *  1 12 14 15
 *  3  4  6  8
 * 10 11 13  0
 *
 * Здесь 0 обозначает пустую клетку, а 1-15 – фишки с соответствующими номерами.
 * Напомним, что "игра в 15" имеет квадратное поле 4х4, по которому двигается 15 фишек,
 * одна клетка всегда остаётся пустой.
 *
 * Цель игры -- упорядочить фишки на игровом поле, приведя позицию к одному из следующих двух состояний:
 *
 *  1  2  3  4          1  2  3  4
 *  5  6  7  8   ИЛИ    5  6  7  8
 *  9 10 11 12          9 10 11 12
 * 13 14 15  0         13 15 14  0
 *
 * Можно математически доказать, что РОВНО ОДНО из этих двух состояний достижимо из любой исходной позиции.
 *
 * Вернуть решение -- список ходов, приводящих исходную позицию к одной из двух упорядоченных.
 * Каждый ход -- это перемена мест фишки с заданным номером с пустой клеткой (0),
 * при этом заданная фишка должна по горизонтали или по вертикали примыкать к пустой клетке (но НЕ по диагонали).
 * К примеру, ход 13 в исходной позиции меняет местами 13 и 0, а ход 11 в той же позиции невозможен.
 *
 * Одно из решений исходной позиции:
 *
 * [8, 6, 14, 12, 4, 11, 13, 14, 12, 4,
 * 7, 5, 1, 3, 11, 7, 3, 11, 7, 12, 6,
 * 15, 4, 9, 2, 4, 9, 3, 5, 2, 3, 9,
 * 15, 8, 14, 13, 12, 7, 11, 5, 7, 6,
 * 9, 15, 8, 14, 13, 9, 15, 7, 6, 12,
 * 9, 13, 14, 15, 12, 11, 10, 9, 13, 14,
 * 15, 12, 11, 10, 9, 13, 14, 15]
 *
 * Перед решением этой задачи НЕОБХОДИМО решить предыдущую
 */
fun fifteenGameSolution(matrix: Matrix<Int>): List<Int> {
    println(matrix)
    val tempMatrix = createMatrix(matrix.height, matrix.width, 0)
    for (i in 0 until tempMatrix.height) {
        for (j in 0 until tempMatrix.width) {
            tempMatrix[Cell(i, j)] = matrix[Cell(i, j)]
        }
    }
    val moves = mutableListOf<Int>()
    val matrixUnlocked = createMatrix(4, 4, true)
    fun Matrix<Boolean>.lock(cell: Cell) {
        this[cell] = false
    }

    fun Matrix<Boolean>.unlock(cell: Cell) {
        this[cell] = true
    }
    
    fun find(value: Int): Cell {
        for (i in 0 until matrix.height) {
            for (j in 0 until matrix.width) {
                if (matrix[i, j] == value) return Cell(i, j)
            }
        }
        throw IllegalStateException()
    }

    fun switch(nonZero: Cell, zero: Cell) {
        val temp = matrix[nonZero]
        matrix[nonZero] = 0
        matrix[zero] = temp
        moves.add(temp)
    }

    fun putZero(next: Cell) {
        val queue = ArrayDeque<Pair<Cell, List<Cell>>>()
        val current = find(0)
        val visited = mutableSetOf(current)
        queue.add(Pair(current, listOf()))
        fun check(cell: Cell, dr: Int, dc: Int, list: List<Cell>) {
            if (cell.row + dr in 0..3 && cell.column + dc in 0..3) {
                val temp = Cell(cell.row + dr, cell.column + dc)
                if (temp !in visited && matrixUnlocked[temp]) {
                    queue.add(Pair(temp, list + cell))
                    visited.add(temp)
                }
            }
        }
        while (queue.isNotEmpty()) {
            val (curr, list) = queue.removeFirst()
            if (curr == next) {
                for ((c1, c2) in list.zipWithNext()) {
                    switch(c2, c1)
                }
                if (list.isNotEmpty()) switch(curr, list.last())
            }
            check(curr, -1, 0, list)
            check(curr, 1, 0, list)
            check(curr, 0, 1, list)
            check(curr, 0, -1, list)
        }
    }

    // 0 3 -> 3 4
    // n 4    0 n
    fun rotateAntiClockWise(upperLeft: Cell) { // independent from matrixLocked
        val upperRight = upperLeft.let { Cell(it.row, it.column + 1) }
        val lowerRight = upperLeft.let { Cell(it.row + 1, it.column + 1) }
        switch(upperRight, upperLeft)
        switch(lowerRight, upperRight)
    }

    // 0 n ->  9 0
    // 9 13   13 n
    fun rotateClockWise(upperLeft: Cell) { // independent from matrixLocked
        val lowerRight = upperLeft.let { Cell(it.row + 1, it.column + 1) }
        val lowerLeft = upperLeft.let { Cell(it.row + 1, it.column) }
        switch(lowerLeft, upperLeft)
        switch(lowerRight, lowerLeft)
    }

    fun putSimple(next: Cell, value: Int) {
        val queue = ArrayDeque<Pair<Cell, List<Cell>>>()
        val current = find(value)
        val visited = mutableSetOf(current)
        queue.add(Pair(current, listOf()))
        fun check(cell: Cell, dr: Int, dc: Int, list: List<Cell>) {
            if (cell.row + dr in 0..3 && cell.column + dc in 0..3) {
                val temp = Cell(cell.row + dr, cell.column + dc)
                if (temp !in visited && matrixUnlocked[temp]) {
                    queue.add(Pair(temp, list + cell))
                    visited.add(temp)
                }
            }
        }
        while (queue.isNotEmpty()) {
            val (curr, list) = queue.removeFirst()
            if (curr == next) {
                for ((c1, c2) in (list + curr).zipWithNext()) {
                    matrixUnlocked.lock(c1)
                    putZero(c2)
                    matrixUnlocked.unlock(c1)
                    switch(c1, c2)
                }
                //switch(curr, list.last())
            }
            check(curr, -1, 0, list)
            check(curr, 1, 0, list)
            check(curr, 0, 1, list)
            check(curr, 0, -1, list)
        }
    }
    //ничего
    putSimple(Cell(0, 0), 1)
    matrixUnlocked.lock(Cell(0, 0))
    //1
    putSimple(Cell(0, 1), 2)
    matrixUnlocked.lock(Cell(0, 1))
    //1, 2
    putSimple(Cell(3, 3), 4)//отгоняю 4 от дырки, где будет 3
    putSimple(Cell(0, 3), 3)
    matrixUnlocked.lock(Cell(0, 3))
    putSimple(Cell(1, 3), 4)
    matrixUnlocked.lock(Cell(1, 3))
    putZero(Cell(0, 2))
    rotateAntiClockWise(Cell(0, 2))
    matrixUnlocked.unlock(Cell(1, 3))
    matrixUnlocked.lock(Cell(0, 2))
    //1, 2, 3, 4
    putSimple(Cell(1, 0), 5)
    matrixUnlocked.lock(Cell(1, 0))
    //1, 2, 3, 4, 5
    putSimple(Cell(3, 3), 13)//отгоняю 13 от дырки, где будет 9
    putSimple(Cell(3, 0), 9)
    matrixUnlocked.lock(Cell(3, 0))
    putSimple(Cell(3, 1), 13)
    matrixUnlocked.lock(Cell(3, 1))
    putZero(Cell(2, 0))
    rotateClockWise(Cell(2, 0))
    matrixUnlocked.unlock(Cell(3, 1))
    matrixUnlocked.lock(Cell(2, 0))
    //1, 2, 3, 4, 5, 9, 13
    putSimple(Cell(1, 1), 6)
    matrixUnlocked.lock(Cell(1, 1))
    //1, 2, 3, 4, 5, 6, 9, 13
    putSimple(Cell(3, 3), 8)//отгоняю 8 от дырки, где будет 7
    putSimple(Cell(1, 3), 7)
    matrixUnlocked.lock(Cell(1, 3))
    putSimple(Cell(2, 3), 8)
    matrixUnlocked.lock(Cell(2, 3))
    putZero(Cell(1, 2))
    rotateAntiClockWise(Cell(1, 2))
    matrixUnlocked.unlock(Cell(2, 3))
    matrixUnlocked.lock(Cell(1, 2))
    //11 в угол, 10 на место, 11 на место, поворот 11 против, повороты оствавшегося квадрата 2x2 до совпадения 12ти (против часовой)
    putSimple(Cell(3, 3), 11)//отгоняю 8 от дырки, где будет 7
    putSimple(Cell(2, 1), 10)
    putSimple(Cell(2, 2), 11)
    putZero(Cell(3, 1))//далее поворот вручную
    switch(Cell(2, 1), Cell(3, 1))
    switch(Cell(2, 2), Cell(2, 1))
    while (matrix[Cell(2, 3)] != 12) {
        rotateAntiClockWise(Cell(2, 2))
        switch(Cell(3,2), Cell(3,3))
        switch(Cell(2,2), Cell(3,2))
    }
    switch(Cell(2,1), Cell(2,2))
    switch(Cell(3, 1), Cell(2, 1))
    switch(Cell(3, 2), Cell(3, 1))
    switch(Cell(3, 3), Cell(3, 2))
    //rotateAntiClockWise(Cell(2, 2))
    println("-------------")
    println(moves)
    println("-------------")
    println(matrix)
    println("-------------")
    println(fifteenGameMoves(tempMatrix, moves))
    return moves
}
