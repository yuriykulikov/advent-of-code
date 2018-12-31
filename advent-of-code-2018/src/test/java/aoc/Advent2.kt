package aoc


import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Advent2 {
    @Test
    fun `Checksum should be 12`() {
        val lines = inputExample.lines()

        val twos = lines.count { it.hasRepeatingChars(2) }
        val threes = lines.count { it.hasRepeatingChars(3) }

        assertThat(twos * threes).isEqualTo(12)
    }

    @Test
    fun `Checksum should be 8715`() {
        val lines = input1.lines()

        val twos = lines.count { it.hasRepeatingChars(2) }
        val threes = lines.count { it.hasRepeatingChars(3) }

        assertThat(twos * threes).isEqualTo(8715)
    }

    @Test
    fun `Similar boxes are fvstwblgqkhpuixdrnevmaycd O(n^2)`() {
        val strings: List<String> = input1.lines()

        // O(n)
        fun String.matchingId(): Pair<String, String>? {
            return strings.mapNotNull { nextId ->
                val foundDifferentByOne = nextId.zip(this) { lhs, rhs -> lhs != rhs }
                    .count { it } == 1
                if (foundDifferentByOne) this to nextId else null
            }.firstOrNull()
        }

        // O(n) * O(n) -> O(n^2)
        val result = strings
            .mapNotNull { it.matchingId() }
            .first()
            .commonPart()

        assertThat(result).isEqualToIgnoringCase("fvstwblgqkhpuixdrnevmaycd")
    }

    @Test
    fun `Similar boxes are fvstwblgqkhpuixdrnevmaycd O(n)`() {
        val strings = input1.lines()

        val positionAndCharToInput = strings
            // This is 0(n) - we only iterate the list once
            .fold(mutableMapOf<Pair<Int, Char>, MutableList<String>>()) { acc, next ->
                next.mapIndexed(::Pair)
                    // add the string to each char to pos list
                    .forEach { acc.getOrPut(it, ::mutableListOf).add(next) }
                acc
            }

        // this is O(1)
        fun tryFindMatch(candidateString: String): Pair<String, String>? {
            return candidateString.mapIndexed(::Pair)
                // strings for every pair into one pile
                .flatMap { positionAndCharToInput[it] ?: mutableListOf() }
                .groupBy { it }
                .values
                .firstOrNull { list -> list.size == candidateString.length - 1 }
                ?.let { candidateString to it[0] }
        }

        // This is 0(n) * O(1) -> O(n)
        val result = strings.mapNotNull { tryFindMatch(it) }.first().commonPart()

        assertThat(result).isEqualToIgnoringCase("fvstwblgqkhpuixdrnevmaycd")
    }

    @Test
    fun `Similar boxes are fvstwblgqkhpuixdrnevmaycd O(n) 2`() {
        val strings = input1.lines()

        val result = strings
            .flatMap { string ->
                (0 until string.length).map {
                    val arr = string.toCharArray()
                    arr[it] = '_'
                    String(arr)
                }
            }
            .groupBy { it }
            .values
            .first { it.size == 2 }
            .let { it.first().replace("_", "") }

        assertThat(result).isEqualToIgnoringCase("fvstwblgqkhpuixdrnevmaycd")
    }

    fun Pair<String, String>.commonPart(): String {
        return this.first
            .zip(this.second) { letterOfFirst, letterOfSecond ->
                if (letterOfFirst == letterOfSecond) letterOfFirst else null
            }
            .filterNotNull()
            .joinToString("")
    }

    private fun String.hasRepeatingChars(what: Int): Boolean {
        val counts = this
            .fold(IntArray(27) { 0 }) { counts, next ->
                counts[next.toInt() - 97] += 1
                counts
            }
        val res: Int? = counts.firstOrNull { it == what }
        return res != null
    }

    val inputExample = """
        abcdef
        bababc
        abbcde
        abcccd
        aabcdd
        abcdee
        ababab
        """.trimIndent().trim()

    val input1 = """
fzostwblnqkhpuzxirnevmaycq
fzastwblgqkupujxirnevmaucd
fzostwbxgakhpujxirnevmayci
uzosmwblgqkhlujxirnevmaycd
bzostwblgqkhpujxirnenfaycd
fzostwblghkhpujxibneemaycd
fzostwblgrkkpujxirnevqaycd
fzostwblgqkhpkjxirqkvmaycd
fzostwblgqdxpujbirnevmaycd
fzostwblgykhfujxirfevmaycd
fzostwbvgskhpujxirnevmagcd
flostwblgokhpuixirnevmaycd
fzostwblgzkhppjxirnevxaycd
ftosuwblgqkhpujxirnevmeycd
fzostkblgqkzpujxirnrvmaycd
fuostwbugqkhnujxirnevmaycd
frostwblgqkhpujsirneamaycd
bzostwblgqkhpajxirnevmuycd
fzysttbluqkhpujxirnevmaycd
czostgblgqkhpujxirnevmgycd
fzostwbcgqkhpujxirnevmavcr
vzostwbdgqrhpujxirnevmaycd
fzostwblgxkhpujxirnekmdycd
fzostwblgdkspzjxirnevmaycd
fzoqtwblgquhpujxirnevkaycd
fzostwblgqjhfujxirnevmayzd
fyostwblgtkhpujxilnevmaycd
izostwblzqkhpupxirnevmaycd
fzoaurblgqkhpujxirnevmaycd
fzostjblgqkhpbjxirbevmaycd
fzostwblgqkhpujmirngvmayca
fxostwbhgqkhiujxirnevmaycd
fzostwblngkhpujxirnemmaycd
fzosgwblgqkhpujxirnlvqaycd
bpostwblgqkhxujxirnevmaycd
fzhstwblgqshpujxirnepmaycd
fzootwbegqkhwujxirnevmaycd
fzmstwblgqkhmujxirnevsaycd
fzostwolgqkhpujxirnevmaysp
foostwblgqohpujxirnevmayyd
fzostwblgqkhgujxgrnevqaycd
fzostwbltqkhjujxipnevmaycd
fsostwblgqkhfujxirnevmayck
fzostwmlgqkbpujxifnevmaycd
fdostwblgqkhsujxirnecmaycd
fzostwblgquhpejxiinevmaycd
fzoqtwblgqkhkujxirnevmaywd
fzostwblmqkmpujxirnyvmaycd
fzjstwblgqkhpuyxirneomaycd
fzortwblgqkhpzjxprnevmaycd
fzosnwulgqkhpujxirnevpaycd
fzostwbegqkvpujxirnevmaykd
fzostwylzqkypujxirnevmaycd
fioshwblgqehpujxirnevmaycd
fzostwbliqkhoujxirnesmaycd
fzostwblgqkhpujdirnelmqycd
fzmstwblgqkhpujxirnnvpaycd
fzostwbtgqkhcujxirnevmaycz
fzostwblgqkfpujeignevmaycd
fzostwbegqkhpuvxirndvmaycd
fzostwblgqkcpujxmrnevmaycp
fvostwblgqkhpyjxirnevmaycx
fzostwblgqkhpufxirnevmzacd
fzisjwblglkhpujxirnevmaycd
fzosowbliqkhgujxirnevmaycd
fzostwblgqkhpujxirnyvmaywl
fzostwbllqnzpujxirnevmaycd
fzostwblgquhpujxirneomayci
fzostwblgqjepujximnevmaycd
fzostwblgqrhbujxijnevmaycd
fzostoblgqkhpujxirnevmoynd
fzortwblgqkhpujxixnevjaycd
fzostwhlgqkapujxirnevmaych
fzostwblgakhpujxirnevfayct
faostwblgqohpljxirnevmaycd
fzostwbhgqkhpujxirnyvcaycd
fjostwblgqkhpwjxirnevjaycd
fzostwblgqklpuixirnevmayxd
fzostwblgqkhhujxirncvmayce
fzostwblzqkhpzjxkrnevmaycd
fzostwblqqkbpujxirnevmcycd
ffostwblxqkhpujxirnevmayxd
fzostwblgqkhpbuxirnefmaycd
szostiblgqkhpujxirnevoaycd
fhlstwblgqkhpujxirnevmaycb
fzostwblgwfhpujxirnevmayed
fyostwblgqkhzujxirnevmayjd
fzostwblgqkvrujxirnevmaucd
fzestwblghkhpujxirnevmaycq
foostwbkgqkhhujxirnevmaycd
fpxstwblgqkhpujxirzevmaycd
fsostwtlgqmhpujxirnevmaycd
feostwelgqkhpumxirnevmaycd
fzostwbvgqkhpujkirnevmayce
fzmsewblgqahpujxirnevmaycd
fzsstsblgkkhpujxirnevmaycd
fzostwblgqkhxajxirneumaycd
fzostwblmqkhpujyisnevmaycd
gzostwblgqkhphjxirnevmavcd
fzostwblgckgpujxirnzvmaycd
qzostdblgqkhpujxirnevmaywd
fzoshwblgskhpufxirnevmaycd
fzosdwblgqkhpujkirnevjaycd
fzvstwblgqkhpuixdrnevmaycd
szostwblgqxhpujeirnevmaycd
fzosvwblgtkhpujxirnevmoycd
fzoscwblgqkkpujxirnevmvycd
fzostsblgqnlpujxirnevmaycd
fzostwblgmkhpujxlrnevjaycd
fzosnwblgqklpugxirnevmaycd
fcostwblgqpmpujxirnevmaycd
ozostwblgbkhpujxirnevmafcd
fzostwbagqkhiujxirneemaycd
fzosxwblgqkhpumxirneymaycd
fpostkblgqkhpujxianevmaycd
fzostwblgvkhhujxirnevmaycl
fzobtwtlgqkhpuwxirnevmaycd
fzostwvwgqkhpujxirtevmaycd
xzostwglgqkhpujtirnevmaycd
fzostwblgvkhtujxirnzvmaycd
jzostwblgqkhpujxirrevmvycd
pzostwbagqkhpujxirnevjaycd
fzostwclgqkhpujxirnhvmeycd
fzostwblglkhpujxirnevmayrf
fzoskwblnqkhpujxirnevmaysd
fzostbblgqkhpujxirnevmjycw
fzostwblggobpujxirnevmaycd
fzostwblgckhpijxirnevmayxd
fiostwrlgqkhpujxirnevmayck
frfstwblvqkhpujxirnevmaycd
fzowvwblgqkhpujsirnevmaycd
fzostwblgquhgujxirnevmiycd
fzoztwblgqkhpujxvrnevmaycj
fzostnbtgqkhpujxixnevmaycd
fzfstwblgjkrpujxirnevmaycd
fzostwblpqkhpdrxirnevmaycd
fivstwblgqkhpuixdrnevmaycd
fzostwbpgqkhpdjxirnewmaycd
fzostwblgqkhpdjxsrngvmaycd
fzostwblsqkhpujxisnxvmaycd
fcosvwblgqkhpujxirnevmavcd
fzostwrlgekhgujxirnevmaycd
fzostwblgqkhpujxindeimaycd
uzostwblgqshpujxirnevmwycd
fzostwzliqkhpujxirnevmaycu
zzostwbtgqkhpijxirnevmaycd
fzoltwblgqkhpujxinncvmaycd
fzostwblgqkzpujxprnevmayhd
fqostwblgqkhpujrirnevmzycd
jzostwblgqkhpuzxkrnevmaycd
fzostwblgqkhpuwxirszvmaycd
fzostwblgqkhpxjxilnevdaycd
fpmstwblgqkhpujxirnwvmaycd
fzoejwblqqkhpujxirnevmaycd
fzostwblgkshzujxirnevmaycd
fzoatqblgqxhpujxirnevmaycd
fzostwblgpkhpujiipnevmaycd
fzostwblgqghpujgbrnevmaycd
izosowblgqkhpujxirnejmaycd
fzostwblgqthpujxjrnevmalcd
fzovtwbcgqkhpujxicnevmaycd
fzoatwblgqkhpujxidntvmaycd
kzpstwblgqihpujxirnevmaycd
fzosawnlgqkhpujxibnevmaycd
fzostwblgqkhpujxarnevdajcd
bzostwblgqkhpujxvrnevmrycd
fzostwblgqkhpwfxirnevmazcd
fzostwblgqknpujyiqnevmaycd
zzostwblgqkhpujyirneqmaycd
flosiwblgqihpujxirnevmaycd
fzoetwblgqkhxujxirnevmeycd
fznstwbugqkhpujxibnevmaycd
fzbstwblgqkhpudxitnevmaycd
fzostwblgqkhkujxirnexaaycd
fzohthblgqkhpujxiknevmaycd
fzostwblgqkhpujxirnevvayjt
fzostwblggkhpujxirnrvqaycd
fzostwblgqslpujxirnevmaysd
aoostwblgqkhpnjxirnevmaycd
fzostwblgqkhlutxirnevuaycd
fxostwbugqkhpujxirnexmaycd
fzoftwblgqkhpsjxirnevmaywd
fzbstwblgqkhndjxirnevmaycd
fzostwblgqkhpxjxipnlvmaycd
fzostwbloqkhowjxirnevmaycd
fzostwblgqkcpdjxirnevnaycd
vzostiblgqkhpsjxirnevmaycd
fzostwblgqkhazjxirnevmaycg
fzostaklgqkhpujxirnevmaypd
fzostwblgkkhppjxirnevpaycd
izostwblgqkhpujairhevmaycd
fzostwdlgqkhpuqxzrnevmaycd
fzostwblgqkepujxernevmayct
fzostdblgqkhpujxyrnehmaycd
fzostwblgqkhsujxirnenfaycd
fzostwblgqkhpujxifnevmajld
fzostwblgokhpujxirxemmaycd
fzastwblcqkhpujxiruevmaycd
fzostwsxgqkhpuexirnevmaycd
xzosxwblgqkhpujxirnetmaycd
fzostwblgqkhpuexirnevmkccd
fzostwblgqklpujxirnermfycd
fzoetwblgqkhpujxirnehhaycd
ffostwblgvkhpujxirnevmazcd
fcosywblgqkhpujxirnevmaycy
fzmstwblgqkhpujxdrnevmaycl
fpostwblgqahpujxirnqvmaycd
fzostwbmgqkhpulxornevmaycd
fzostwblgqkopujxqrnevmrycd
fzostwblgqkhpujxisnevmjgcd
fzogtwulfqkhpujxirnevmaycd
fzostwalgqkhpcjxirnevmayud
fzosxwblgqkhpujxirnevmasmd
fzostwblgrkowujxirnevmaycd
fzostsblgqkhpujxirnevmsccd
fzostwblgqkhpujxfrnnvmaocd
fzostwblgqkhpujxiynsvkaycd
fzosowblgqkhpwjxirnevmaecd
fzosgwblgfkhpujxyrnevmaycd
fzostsblgzkhpujmirnevmaycd
fzostwblrqkhyuixirnevmaycd
qzostwblgqkhpujxyrnevmvycd
lzostwblgqkhpujxirqevmaymd
fzostwblgqkbpujxirnefbaycd
fzostwblgmihpujxirnevmafcd
fzostmblgqkhpujxirnevmpynd
fzoltwblgqkhpujlihnevmaycd
fzostwblgqkhpujdirneviyycd
fzgspwblgqkhpqjxirnevmaycd
fzostwblgqkhtujkirnevmayld
fjostwblgqkhpagxirnevmaycd
fzpsthblgqkhpuzxirnevmaycd
fzostwblgqkhpuhxzrneymaycd
fzoftwblgqkepujxirnevcaycd
fzostwblgqkbpyjxipnevmaycd
fzostwqlgqkhpujxirjevmayad
fzoxtwblgqkypupxirnevmaycd
fzostwblgqwhpuoxiynevmaycd
fzostwblgqkhpfjkirnevmavcd
fzoqtwblgqkhpujxirnermaycp
fzostwbngxkhpujxirnevmayqd
fzostwqogqkhpcjxirnevmaycd
fzostwblyqkhpujxvrnevmayzd
vzostwblgqkhpujxirnevmlzcd
fzostwblaqkhpujxirnevbajcd
fdostwblgqkhpujxzrnevmayod
fzostxbloqkhpujxirnevmcycd
fzostwbcgqkhpyjxirnegmaycd
fzostwblgqkhpuyxirnzrmaycd
fzustwycgqkhpujxirnevmaycd
""".trimIndent()
}
