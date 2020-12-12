package aoc

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day12RainRisk {
    private data class Ship(
        val pos: Point,
        val direction: Int,
    ) {
        fun move(where: Point): Ship = copy(pos = pos + where)
        fun rotate(deg: Int): Ship = copy(direction = direction + deg)

        fun moveForward(num: Int): Ship {
            val angle = direction.rem(360).let { if (it < 0) 360 + it else it }

            return when (angle) {
                90 -> move(Point(x = 0, y = num))
                270 -> move(Point(x = 0, y = -num))
                0 -> move(Point(x = num, y = 0))
                180 -> move(Point(x = -num, y = 0))
                else -> error("Unknown direction: $direction")
            }
        }
    }

    @Test
    fun silverTest() {
        val positions = takeEvasiveActions(testInput)

        printMap(positions
            .map { ship -> ship.pos to '*' }
            .toMap(), invertedY = false)

        assertThat(positions.last().pos.manhattan()).isEqualTo(25)
    }

    @Test
    fun silver() {
        val positions = takeEvasiveActions(taskInput)
        assertThat(positions.last().pos.manhattan()).isEqualTo(2879)
    }

    private fun takeEvasiveActions(input: String): List<Ship> {
        val instructions = input.lines()
            .filter { it.isNotBlank() }
            .map { it.first() to it.substring(1).toInt() }

        val positions = instructions.scan(Ship(pos = Point(0, 0), direction = 0)) { ship, (what, num) ->
            ship.takeAction(what, num)
        }
        return positions
    }

    private fun Ship.takeAction(what: Char, num: Int): Ship {
        return when (what) {
            'N' -> move(Point(x = 0, y = -num))
            'S' -> move(Point(x = 0, y = num))
            'E' -> move(Point(x = num, y = 0))
            'W' -> move(Point(x = -num, y = 0))
            'L' -> rotate(-num)
            'R' -> rotate(num)
            'F' -> moveForward(num)
            else -> error("wtf: $what")
        }
    }

    data class WaypointShip(
        val pos: Point,
        val waypoint: Point,
    ) {
        fun moveWaypoint(where: Point): WaypointShip = copy(waypoint = waypoint + where)

        fun rotateWaypoint(deg: Int): WaypointShip {
            return when (deg) {
                90, -270 -> copy(waypoint = waypoint.rotateCCW())
                180, -180 -> copy(waypoint = waypoint.rotateCCW().rotateCCW())
                270, -90 -> copy(waypoint = waypoint.rotateCW())
                else -> error("unsupported angle: $deg")
            }
        }

        fun moveForward(num: Int): WaypointShip {
            return copy(pos = pos + waypoint * num)
        }
    }

    @Test
    fun goldTest() {
        val positions = takeEvasiveActionsWithWaypoint(testInput)

        printMap(positions
            .map { ship -> ship.pos to '*' }
            .toMap(), invertedY = false)

        assertThat(positions.last().pos.manhattan()).isEqualTo(286)
    }

    @Test
    fun gold() {
        val positions = takeEvasiveActionsWithWaypoint(taskInput)
        assertThat(positions.last().pos.manhattan()).isEqualTo(178986)
    }

    private fun takeEvasiveActionsWithWaypoint(input: String): List<WaypointShip> {
        val instructions = input.lines()
            .filter { it.isNotBlank() }
            .map { it.first() to it.substring(1).toInt() }

        val initial = WaypointShip(
            pos = Point(0, 0),
            waypoint = Point(10, -1)
        )

        return instructions.scan(initial) { ship, (what, num) ->
            ship.takeAction(what, num)
        }
    }

    private fun WaypointShip.takeAction(what: Char, num: Int): WaypointShip {
        return when (what) {
            'N' -> moveWaypoint(Point(x = 0, y = -num))
            'S' -> moveWaypoint(Point(x = 0, y = num))
            'E' -> moveWaypoint(Point(x = num, y = 0))
            'W' -> moveWaypoint(Point(x = -num, y = 0))
            'L' -> rotateWaypoint(-num)
            'R' -> rotateWaypoint(num)
            'F' -> moveForward(num)
            else -> error("wtf: $what")
        }
    }

    private val testInput = """
        F10
        N3
        F7
        R90
        F11
    """.trimIndent()

    private val taskInput = """
        F75
        L90
        N5
        W2
        N5
        L90
        S1
        R90
        S4
        E2
        W1
        F100
        L180
        W3
        N1
        R180
        W1
        S4
        R270
        S4
        F23
        E2
        F17
        N5
        R90
        F83
        E2
        F34
        E3
        S4
        F19
        W4
        L90
        W2
        L90
        E2
        F79
        L90
        F80
        N1
        R90
        F35
        L90
        N5
        E3
        S5
        R90
        F71
        N1
        F19
        F49
        R90
        E4
        L90
        W1
        R90
        S2
        E1
        L180
        F79
        W4
        L180
        F74
        W5
        R90
        F30
        R90
        W2
        R90
        F35
        R90
        F12
        W2
        F7
        R90
        E5
        F68
        E3
        F21
        N4
        F22
        W4
        S2
        F89
        L90
        N5
        R90
        F8
        R90
        W2
        S3
        R180
        N2
        R90
        F49
        N4
        E5
        F31
        S3
        E4
        F61
        E1
        L90
        N1
        W5
        F15
        F13
        E4
        N2
        R180
        S2
        L90
        F92
        S4
        R180
        L90
        S1
        L90
        F98
        N4
        E3
        F69
        S3
        R180
        F59
        S5
        R90
        F32
        E5
        S4
        W4
        F9
        R180
        F44
        L90
        W1
        L180
        F74
        W1
        F55
        R270
        S3
        F62
        W3
        F61
        N2
        L90
        F43
        S3
        F86
        W1
        S3
        E3
        F34
        L90
        F77
        N1
        F27
        N5
        W5
        N3
        F80
        E2
        L180
        F59
        W1
        L90
        N4
        R90
        W5
        L180
        W5
        L90
        F17
        E5
        L180
        W4
        R180
        W4
        F70
        S5
        R90
        W4
        W4
        N3
        W1
        N2
        E5
        F53
        N5
        R90
        R90
        E2
        N5
        R270
        S5
        R90
        N5
        E1
        R90
        F76
        R90
        W3
        S2
        R90
        S5
        E1
        L90
        E2
        F58
        R90
        F15
        N2
        L90
        N5
        F65
        E5
        N2
        R90
        W1
        F40
        S4
        R90
        F23
        W4
        R90
        W4
        F17
        S2
        R90
        E4
        F58
        R90
        L90
        F64
        N4
        F63
        E5
        N1
        F38
        N3
        F19
        E5
        S4
        F14
        R90
        S3
        E2
        S4
        E1
        F12
        W3
        L90
        F33
        S4
        W3
        F29
        R90
        N3
        F44
        E5
        N1
        F49
        W5
        F52
        N5
        L90
        S4
        F31
        R90
        N3
        L270
        L270
        E4
        N4
        W1
        L90
        S5
        W4
        R180
        N3
        L180
        F2
        S1
        L180
        N3
        L90
        N1
        W4
        R180
        W5
        R270
        N4
        L180
        F51
        R180
        S5
        F35
        E3
        R180
        F1
        S3
        E2
        F47
        E4
        S5
        E1
        L90
        F16
        S4
        F17
        N1
        W2
        L90
        F99
        E1
        R180
        N1
        F78
        L90
        F18
        S2
        W3
        N2
        F56
        R270
        W1
        R90
        F53
        L180
        F6
        W5
        R90
        W4
        F27
        R90
        F73
        E5
        R90
        W4
        S4
        W3
        S2
        E5
        N5
        R270
        F43
        W1
        F77
        E4
        F85
        W2
        R90
        N1
        E1
        F39
        R90
        W2
        N4
        E2
        W4
        F81
        W4
        F3
        W4
        R180
        W2
        N5
        R90
        N5
        E2
        S5
        S3
        E1
        N4
        W1
        S3
        F48
        S2
        F23
        E2
        R180
        F89
        L180
        N3
        E1
        N5
        W2
        L90
        E1
        F80
        R90
        S1
        E3
        N5
        L90
        F11
        W5
        F70
        N3
        W1
        L90
        N5
        F99
        N5
        F24
        R180
        E4
        F57
        S1
        F9
        S5
        R90
        W4
        F16
        E1
        F18
        F17
        L180
        S1
        L90
        S1
        E2
        N1
        E4
        F84
        N1
        F6
        E1
        L180
        W3
        L180
        N3
        F60
        F43
        S3
        F48
        W2
        S1
        R180
        N1
        S2
        F78
        L180
        W4
        F51
        W5
        N2
        E2
        N1
        W4
        F65
        N3
        L90
        W3
        N2
        W2
        F46
        N4
        W4
        N2
        W2
        F74
        R180
        E4
        R90
        N1
        F33
        S4
        L180
        S1
        F15
        R180
        S5
        L180
        F21
        N4
        F87
        W4
        L90
        S4
        F40
        S5
        W3
        L180
        F78
        W2
        F40
        N3
        E3
        S4
        W1
        S4
        E1
        S4
        W5
        S2
        F82
        E5
        R180
        F7
        W3
        R90
        N2
        R90
        W5
        N5
        W5
        S3
        E2
        L90
        E1
        F77
        E4
        F70
        W5
        S2
        W2
        F7
        W4
        F8
        R180
        W5
        L90
        F49
        W2
        L90
        S5
        W1
        S3
        F74
        W3
        F76
        W3
        N1
        E4
        S2
        F65
        E1
        W1
        F85
        E5
        F67
        R90
        W1
        F8
        R180
        N2
        F94
        W4
        R90
        S4
        F81
        N5
        R90
        E4
        F9
        N3
        F49
        R180
        N3
        F94
        N4
        R90
        S2
        F34
        W2
        E4
        W5
        F84
        S4
        W1
        R180
        W5
        F13
        W3
        L90
        N1
        E1
        R90
        S2
        R180
        F81
        L90
        F36
        W3
        S4
        W1
        F3
        R90
        W3
        R90
        E5
        R90
        F46
        L90
        F63
        N1
        L90
        W4
        N1
        S2
        N3
        L90
        F64
        W5
        N4
        F45
        W4
        S4
        R90
        W3
        N5
        R90
        L180
        E4
        R270
        S1
        L180
        E1
        F22
        E4
        F11
        W3
        F12
        W3
        R90
        S5
        E3
        F4
        R90
        S3
        F3
        L180
        S2
        F16
        E5
        R90
        F75
        W4
        N1
        S4
        F81
        L90
        F69
        N5
        L90
        W5
        F63
        N3
        W2
        L90
        F39
        E5
        F81
        W3
        F1
        L90
        F31
        W4
        S4
        F32
        W3
        N4
        L180
        N5
        W2
        F20
        L90
        F42
        S3
        E5
        S4
        L90
        S2
        L90
        S3
        W5
        F54
        R90
        F85
        S4
        F22
        W3
        F21
        R90
        F66
        S1
        F22
        S4
        W5
        N5
        E3
        F52
        L180
        W3
        S1
        W5
        R180
        F43
        E5
        F16
        W4
        S5
        W5
        N4
        F97
        E5
        F4
        E2
        R180
        F99
        S5
        F44
        R90
        W5
        N2
        L90
        F46
        E4
        L180
        E4
        R90
        F45
        R90
        S4
        F30
        R90
        F23
        N5
        W2
        F45
        S3
        F70
        L90
        W2
        F90
        W5
        F81
        E2
        F15
        L90
        F90
        N2
        F78
        S4
        F3
        F37
    """.trimIndent()
}