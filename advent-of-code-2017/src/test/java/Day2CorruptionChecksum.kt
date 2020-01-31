import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day2CorruptionChecksum {
    private fun checkSumOf(exampleInput: String): Int {
        return exampleInput.lines()
            .map { line ->
                val ints = line.split("\t", " ").filter { it.isNotBlank() }.map { it.toInt() }
                ints.max()!! - ints.min()!!
            }
            .sum()
    }

    @Test
    fun `checksum is 18`() {
        assertThat(checkSumOf(exampleInput)).isEqualTo(18)
    }

    @Test
    fun `checksum of test is 53460`() {
        assertThat(checkSumOf(testInput)).isEqualTo(53460)
    }

    private fun evenlyDivided(input: String): Int {
        val lines: List<List<Int>> = input.lines()
            .map { line ->
                line.split("\t", " ")
                    .filter { it.isNotBlank() }
                    .map { it.toInt() }
            }

        return lines.map { ints ->
            val permutations = ints.flatMap { number -> ints.map { it to number } }
                .filter { (f, s) -> f != s }
                .map { (f, s) -> if (f > s) f to s else s to f }
                .distinct()

            permutations
                .first { (f, s) -> f.rem(s) == 0 }
                .let { (f, s) -> f / s }
        }.sum()
    }

    private val exampleInput2 = """
    5 9 2 8
    9 4 7 3
    3 8 6 5
""".trimIndent()

    @Test
    fun `evenly divided is 9`() {
        assertThat(evenlyDivided(exampleInput2)).isEqualTo(9)
    }

    @Test
    fun `evenly divided testInput is 282`() {
        assertThat(evenlyDivided(testInput)).isEqualTo(282)
    }

    private val exampleInput = """
5 1 9 5
7 5 3
2 4 6	8
    """.trimIndent()

    private val testInput = """
        1364	461	1438	1456	818	999	105	1065	314	99	1353	148	837	590	404	123
204	99	235	2281	2848	3307	1447	3848	3681	963	3525	525	288	278	3059	821
280	311	100	287	265	383	204	380	90	377	398	99	194	297	399	87
7698	2334	7693	218	7344	3887	3423	7287	7700	2447	7412	6147	231	1066	248	208
3740	837	4144	123	155	2494	1706	4150	183	4198	1221	4061	95	148	3460	550
1376	1462	73	968	95	1721	544	982	829	1868	1683	618	82	1660	83	1778
197	2295	5475	2886	2646	186	5925	237	3034	5897	1477	196	1778	3496	5041	3314
179	2949	3197	2745	1341	3128	1580	184	1026	147	2692	212	2487	2947	3547	1120
460	73	52	373	41	133	671	61	634	62	715	644	182	524	648	320
169	207	5529	4820	248	6210	255	6342	4366	5775	5472	3954	3791	1311	7074	5729
5965	7445	2317	196	1886	3638	266	6068	6179	6333	229	230	1791	6900	3108	5827
212	249	226	129	196	245	187	332	111	126	184	99	276	93	222	56
51	592	426	66	594	406	577	25	265	578	522	57	547	65	564	622
215	2092	1603	1001	940	2054	245	2685	206	1043	2808	208	194	2339	2028	2580
378	171	155	1100	184	937	792	1436	1734	179	1611	1349	647	1778	1723	1709
4463	4757	201	186	3812	2413	2085	4685	5294	5755	2898	200	5536	5226	1028	180
    """.trimIndent()
}