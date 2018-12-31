package aoc

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

data class Step(val parent: String, val child: String)

fun parseSteps(input: String): List<Step> {
    return input.lines()
        .map { line ->
            Step(line.substring(5, 6), line.substring(36, 37))
        }
}

class Advent7 {
    @Test
    fun `Test order is CABDFE`() {
        val result: String = findAssemblySequence(Tasks(parseSteps(testSleighInstructions)))
        assertThat(result).isEqualTo("CABDFE")
    }

    @Test
    fun `Star 1 order is BGJCNLQUYIFMOEZTADKSPVXRHW`() {
        val result: String = findAssemblySequence(Tasks(parseSteps(sleighInstructions)))
        assertThat(result).isEqualTo("BGJCNLQUYIFMOEZTADKSPVXRHW")
    }

    enum class State {
        TODO, DOING, DONE
    }

    private fun findAssemblySequence(tasks: Tasks): String {
        val sequence: MutableList<String> = mutableListOf()

        while (tasks.workableTasks().isNotEmpty()) {
            val nextNode = tasks.workableTasks().first()

            sequence.add(nextNode)
            tasks.complete(nextNode)
        }

        return sequence.joinToString("")
    }

    @Test
    fun `Star 2 Test time is 15`() {
        val result: Int = parallelExecution(Tasks(parseSteps(testSleighInstructions)), 2, 0)
        assertThat(result).isEqualTo(15)
    }

    @Test
    fun `Star 2 time is 1017`() {
        val result: Int = parallelExecution(Tasks(parseSteps(sleighInstructions)), 5, 60)
        assertThat(result).isEqualTo(1017)
    }

    /**
     * Mutable structure representing the tasks which are to be done
     */
    class Tasks(steps: List<Step>) {
        private val childToParent = steps.groupBy { step -> step.child }
            .mapValues { entry -> entry.value.map { it.parent } }

        private val tasks: MutableMap<String, State> = steps.map { it.parent }.plus(steps.map { it.child }).distinct()
            .fold(mutableMapOf()) { map, name ->
                map[name] = State.TODO
                map
            }

        fun workableTasks(): List<String> {
            return tasks.entries
                .filter { (key, value) ->
                    // all nodes with fulfilled parents
                    val allParentsFullfilled = childToParent[key]?.all { parent -> tasks[parent] == State.DONE }
                        ?: true
                    allParentsFullfilled && tasks[key] == State.TODO
                }
                .map { it.key }
                .sorted()
        }

        fun complete(nextNode: String) {
            tasks[nextNode] = State.DONE
        }

        fun startProgress(task: String) {
            tasks[task] = State.DOING
        }
    }

    /**
     * Elf agent which assembles the stuff
     */
    class Worker(private val offset: Int) {
        private var task: String? = null
        private var started: Int = 0
        private var requiredTime: Int = 0
        fun assignTask(task: String, time: Int) {
            this.task = task
            this.started = time
            this.requiredTime = task.let { it.toCharArray()[0] - 'A' + 1 + offset }
        }

        fun tick(time: Int): String? {
            val elapsed = time - started
            return task?.let {
                if (requiredTime == elapsed) {
                    task = null
                    it
                } else null
            }
        }

        fun isFree(): Boolean {
            return task == null
        }

        fun dump(): String {
            return task ?: "."
        }
    }

    private fun parallelExecution(tasks: Tasks, howManyWorkers: Int, offset: Int): Int {
        val workers = Array(howManyWorkers) { Worker(offset) }
        var ticker = 0

        generateSequence { ticker++ }.forEach { tick ->
            val doneTasks = workers.mapNotNull { worker -> worker.tick(tick) }
            doneTasks.forEach { tasks.complete(it) }

            val freeWorkers = workers.filter { it.isFree() }
            tasks.workableTasks()
                .zip(freeWorkers)
                .forEach { (task, worker) ->
                    tasks.startProgress(task)
                    worker.assignTask(task, tick)
                }

            if (workers.all { it.isFree() }) {
                return tick
            } else {
                println("$tick ${workers.joinToString(" ") { it.dump() }}")
            }
        }
        throw RuntimeException("Not possible to get here!")
    }
}

private val testSleighInstructions = """
Step C must be finished before step A can begin.
Step C must be finished before step F can begin.
Step A must be finished before step B can begin.
Step A must be finished before step D can begin.
Step B must be finished before step E can begin.
Step D must be finished before step E can begin.
Step F must be finished before step E can begin.
""".trimIndent()

private val sleighInstructions = """
Step B must be finished before step G can begin.
Step G must be finished before step J can begin.
Step J must be finished before step F can begin.
Step U must be finished before step Z can begin.
Step C must be finished before step M can begin.
Step Y must be finished before step I can begin.
Step Q must be finished before step A can begin.
Step N must be finished before step L can begin.
Step O must be finished before step A can begin.
Step Z must be finished before step T can begin.
Step I must be finished before step H can begin.
Step L must be finished before step W can begin.
Step F must be finished before step W can begin.
Step T must be finished before step X can begin.
Step A must be finished before step X can begin.
Step K must be finished before step X can begin.
Step S must be finished before step P can begin.
Step M must be finished before step E can begin.
Step E must be finished before step W can begin.
Step D must be finished before step P can begin.
Step P must be finished before step W can begin.
Step X must be finished before step H can begin.
Step V must be finished before step W can begin.
Step R must be finished before step H can begin.
Step H must be finished before step W can begin.
Step N must be finished before step I can begin.
Step X must be finished before step R can begin.
Step D must be finished before step V can begin.
Step V must be finished before step R can begin.
Step F must be finished before step K can begin.
Step P must be finished before step R can begin.
Step P must be finished before step V can begin.
Step S must be finished before step X can begin.
Step I must be finished before step S can begin.
Step J must be finished before step N can begin.
Step T must be finished before step S can begin.
Step T must be finished before step R can begin.
Step K must be finished before step P can begin.
Step N must be finished before step R can begin.
Step G must be finished before step T can begin.
Step I must be finished before step V can begin.
Step G must be finished before step Q can begin.
Step D must be finished before step H can begin.
Step V must be finished before step H can begin.
Step T must be finished before step K can begin.
Step T must be finished before step W can begin.
Step E must be finished before step H can begin.
Step C must be finished before step R can begin.
Step L must be finished before step K can begin.
Step G must be finished before step Y can begin.
Step Y must be finished before step O can begin.
Step O must be finished before step E can begin.
Step U must be finished before step S can begin.
Step X must be finished before step W can begin.
Step C must be finished before step D can begin.
Step E must be finished before step P can begin.
Step B must be finished before step R can begin.
Step F must be finished before step R can begin.
Step A must be finished before step D can begin.
Step G must be finished before step M can begin.
Step B must be finished before step Q can begin.
Step Q must be finished before step V can begin.
Step B must be finished before step W can begin.
Step S must be finished before step H can begin.
Step P must be finished before step X can begin.
Step I must be finished before step M can begin.
Step A must be finished before step S can begin.
Step M must be finished before step X can begin.
Step L must be finished before step S can begin.
Step S must be finished before step W can begin.
Step L must be finished before step V can begin.
Step Z must be finished before step X can begin.
Step M must be finished before step R can begin.
Step T must be finished before step A can begin.
Step N must be finished before step V can begin.
Step M must be finished before step H can begin.
Step E must be finished before step D can begin.
Step F must be finished before step V can begin.
Step B must be finished before step O can begin.
Step G must be finished before step U can begin.
Step J must be finished before step C can begin.
Step G must be finished before step F can begin.
Step Y must be finished before step M can begin.
Step F must be finished before step D can begin.
Step M must be finished before step P can begin.
Step F must be finished before step T can begin.
Step G must be finished before step A can begin.
Step G must be finished before step Z can begin.
Step K must be finished before step V can begin.
Step J must be finished before step Z can begin.
Step O must be finished before step Z can begin.
Step B must be finished before step E can begin.
Step Z must be finished before step V can begin.
Step Q must be finished before step O can begin.
Step J must be finished before step D can begin.
Step Y must be finished before step E can begin.
Step D must be finished before step R can begin.
Step I must be finished before step F can begin.
Step M must be finished before step V can begin.
Step I must be finished before step D can begin.
Step O must be finished before step P can begin.
""".trimIndent()