import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day3RucksackReorganization {
  @Test
  fun silverTest() {
    silverScore(testInput) shouldBe 157
  }
  @Test
  fun silver() {
    silverScore(input) shouldBe 7674
  }
  private fun silverScore(input: String): Int {
    return input
        .lines()
        .asSequence()
        .filter { it.isNotBlank() }
        .map { line ->
          val asArray = line.toCharArray()
          val left = asArray.take(asArray.size / 2)
          val right = asArray.drop(asArray.size / 2)
          left.intersect(right.toSet()).apply { check(size == 1) }.first()
        }
        .sumOf { it.itemPriority() }
  }
  @Test
  fun goldTest() {
    goldScore(testInput) shouldBe 70
  }
  @Test
  fun gold() {
    goldScore(input) shouldBe 2805
  }

  private fun goldScore(input: String): Int {
    return input
        .lines()
        .asSequence()
        .filter { it.isNotBlank() }
        .windowed(3, 3)
        .map { lines: List<String> ->
          lines
              .map { it.toCharArray().toSet() }
              .reduce { acc, next -> acc.intersect(next) }
              .apply { check(size == 1) }
              .first()
        }
        .sumOf { it.itemPriority() }
  }

  private fun Char.itemPriority() =
      when (this) {
        in 'a'..'z' -> this - 'a' + 1
        in 'A'..'Z' -> this - 'A' + 27
        else -> error("")
      }
}

private val testInput =
    """
    vJrwpWtwJgWrhcsFMMfFFhFp
    jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
    PmmdzqPrVvPwwTWBwg
    wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
    ttgJtRGJQctTZtZT
    CrZsJsPPZsGzwwsLwLmpwMDw
""".trimIndent()

private val input =
    """
    hqBqJsqHhHvhHHqlBvlfpHQQwLVzVwtVzjzttjQVSjMjwL
    gRTRnCRsFNGbTzLjwcSTMmSz
    dGgsRWPGdWgZJqBBqhfpPq
    HNCNJHCWJRRLqNJWlfrrrwsmlwGmmf
    dddvLdLjdDvjvswlmGwlZQtjrt
    dvcpbLVcvNJJHNPHSp
    QDprSpLQRLQrQDmQcQFZjbbhZwdRsRFbbBss
    gWGGPgNvJlgJzDlNGHCGPNZZgFBbsjbFFBwZwfhdZbZB
    lHTlGMHlGCPNvClzGzJHvGcrMcVtLqMLcrrQVcVDrqrm
    SrBpJFfldlFNslFJBZwsmwgVGGsCCVmZZD
    jLtjvzLQMtWjbbQvDZZwGRJGgwggGZgv
    MzqqjznQPqnnjznnctnFlSddPfHflhfBJFNdHd
    mPNNGVCRngnSbgNw
    WqsqlTssgvqvZWZzhsTzWhScdHtfJJnfbtSJwfczdtSS
    sqTvhpqQvWZQLmDpDGMDGrgL
    DQRcLQVLbbcbrFPcRtTBBBJnTZrrnmZTrMgJ
    slGjjjdlhMfvdMQTvg
    jlzNhWHhhWjHlwwwGLDSDtPQVtRzRbSzpp
    DzDgfvzfDczfHCHSlgHLCmWG
    PrpNPJtpPMBssmmVdmSVVr
    PSRMwPnMpBNtNBTnnZwDqzQFfwhjZZqfhh
    fzfBwhBJFbCwbwwg
    strtgtrPgmPgFRsMdRnZRMFn
    mrmLPDvGmmtSLDgmSqvHchhcHQzcQQJHBfBh
    NsgwPPDgsPHqsTqqmLbLrDRhmrRVrbVW
    lFpGFtfFlvSFplGFzptSGSSlhZbhjhfrmWbhLhmLbCZVZjWr
    ccplJQSGcSSpVFvNqqsqPNqPQPTwqd
    HFhPNNZGqSZrCDBVZBCB
    RJTtwczTzCRVQrRHpR
    mfgblfltwgwwmlJgcHNnGhSbFNnFsFjFqG
    tSRqNRHpHnMSTqpcmrWWfqfmrCQCrW
    lhGDtbgVVgDsDbhfldfwrzrfcdzwcr
    vtDDhsgFBTNZFBHS
    LcNpLLBNgdmHGmsBCrRBQDDwnwRj
    hSWfZVhfPJJhfVWbhzbnQqQnRRRqPvCCrjvjwj
    ztJSSJhzzSTJrZSbzzJTfzbZmLmggdmNMFNpmtmMGFFGNpmt
    TRdFfLbTnLvZVlZvznQV
    pgJGprJNhghhNjjPgPNrhNqqlVVlQVVQqQjfzBfQvZzB
    rmGmSwJPwJprrNDbsfMRsdTDcDWHfM
    QMpZZTtwMBttJMMbVqPpqLqbVlbqqN
    rjCFGrdGRwdDHSnqflPVLqqVNllrfl
    DHGDRvhvwTZhJWBQ
    LcQCCpLQVhrdcFQCJrmmmwDwvDtJJnnw
    TsZqqTzMRqZjfsjTTDjsNJnmnStRSHnnSJmnJSNH
    sMMZZbDjZFdbVCFphV
    FCcdFFGBsdDrbMNSmSdmQR
    tVttHVLhvVgfTTtffNSMQRMZSRmMQNmHSb
    gngvvwJtVVTvVvvvfwvJThhCzGcnsCjmCDmscPcGCsGsmz
    fsnzRNZswZszPRZSLflPpDhlhvgWHmjWvJjh
    bVcCqjbdjbcMdBrddrQphvHHmHWlggpWpppqJJ
    FBTdFQGrTjLNsNtL
    MSWWgMdgdbWbbfdgsPmddgCmSLZvSFvLQvnLrFNZvZFLlLLl
    hjpJjGthjRNRptwJJqVBllnvrrlTTQFZqnTZnn
    tjDGHwhHHjwjjJGpwjwjjJpwsdgffmbgMsmbDNcMcdmCPdsm
    MlDrrgnTDLlCCmCRFgRSCR
    HHhbbNQMccQFSNBmmpJNSJ
    MwbMbshsswVbHQsbcVMcrtDllTlLqfTGVzLGrTlT
    gSFzqQTpmVpQVpLFLrzJJRthlsQBlPsZsBhZst
    rwrNDdbHdBhRhZbsjB
    vvvMHwHcwGCwwNfMVSrqffWpSfSFzLmT
    RNgMgRCCgCfPNfvNgVQmhPVWWjWjLLdLBj
    JhJqqqwGDchsBVbdjldmBLmG
    schZzDwTJzFTsctHFMtfftgMttpM
    GHHWqWFWfWHqbRWsFZFmqZbhNjNDNppNjrjDcQdbpddhjr
    wCLCVPfwgVSnPNrQhnDcjcNpDD
    TPgLlggJLVwPVVPPwgTwvtSCFmzGGqzGZsGRqWBGfFRFJFRB
    LfFLmGTPHBfpHmzBLdZfBfZTbWWttWSDJtWVDJDtSWJzVCCV
    RssRRRhrRwQqMQQwnPngQrwvVSjttjJSjSVtWWCWjbVb
    hQPnnRnQgNcQqqQQcsZTNTLpdZZfpZFHNpBp
    VTCVVnwfFTvFmTCvWwJHdlhHWBJhJBRWNHgh
    ZbSMZbctGtScQSZsSpZpPpplhBhtdJDRDJjhdtlRJRjNRj
    rLsbBQSMBGspPGcMPQvnqmwzwmCVLmqVmwTF
    GVrrQVHHHQGTllQjPHGrlCQpZZpJFWZFzzjBssDJghzhFZ
    NSMmWmtqMWqSNbcctdLcdghpgpppssZgbgzpFzJJFs
    cfLqLwMRdtNLMlQrvflWTQPffQ
    FjtGflGsbNqjsmjGGGbmqQQQBQBTjdpTpJTWvJBddj
    CPrVRMnvvLSRHLnBpwdWTRBQwQzBzR
    SnZMZPcHMlqchsvGGb
    nnJnswzPCtmZDCpmhphD
    rSVVLQQQGQjwpdmdNmpS
    VvBcQvGcQgGLBgWrwznfJsncltJsJnssPM
    dhbwLStzSGmmmzJJvFgJNvnrgvhv
    VTsTsRscRsVBMRVTTsjZVPCnDNvfrPfDvNCfBrPNDJ
    scHHQTpJZjHwzLSHHtqHSt
    MHmFsBDmGpGTBfmCfWCffhzgvf
    wcjwnRLPZRVbtCjtNttGCh
    ZLGVJRrnPPPwQwPppqDHFpBFTpqFDr
    GzMgVfGRdRVngDjhqcjctrtrzzzs
    QbQSHwHSLbWwJJFwJPLPSWTQjrhqtjflThccsclCcscCCmmj
    JFbNSbLvHHLQFLvwQJFWSBVgMNGdBBnBMVfddpGngB
    sZHNJwMsvHswwvMWqBzhChWPMBzd
    bHQQQRDHRcRcDljttBldlPBdggBCqB
    QjnDbDjjjncRjbQnfZpvssZNHnppFNpvvp
    NdmfPNMHMdNMHcffHBCwwsvsRRllvwlcqjwscw
    VLhVGLpLShgvrjwFhjqfls
    pppnnQtVpGZVWtzGgVzgSSfnCCDmdPBMDbmmDMmdNMBmWNmm
    rrfgCrrMllfnBhBrdCFfWLFmmVFVWQvWwtwV
    TmSGSqNzvwwTFVvj
    NSGSmNbDzpmSpbHmSJqqlMgdMndcgdccdpgllRRh
    bbPtLnLcLJzTjcJbbTcttLcSgwmvWfVvfVvBgfmWVJMJMJMW
    ZsDNNGDRsrHzpNRwMlgRlgmMWvffWM
    FGzQpZGGLFjnSFbF
    DQZVDdWWNBtgWnJCnL
    SSHSmbHRFqGrmqJrbbGzjmzCwrvgvwnBwnCvprwvpwwPvv
    qFFFSmsRRzzFjcFsSsjmzJzflhlZTTThdhVflZlVDDhMcN
    cdvrFddqDtDvqgCDtFtrvvvFSmShPShJVJmMJSJbVBgTlmbM
    GzjpHzGHHfQNfJSlPQbJBSTJBM
    HWpspsWjNsGRHfpwNszzLfGqnCnZtdvvcZvwqFCcZqMZCv
    rhHhDhhDrRNwDRhNRLRqNPHjHSpVpVlljSSMnlHnjS
    sVVBsFBJBtBSPSjtjPPZZS
    JmddVTzsffcGNwzCqNqR
    nQfqFtZWFZnFJWfpGqhDsGLLPLVbrPhr
    CzjwMTgBgTNSGLVVDPbjbS
    wMCmgNNdgvzGdRQcFGFdfJ
    CbqCDnwFpDpCVfBPmPffPfRfpB
    svcsjlGJGnlnvjvzgQQgGzsZPhBRcRPhWfBZBhPhWRRhPN
    sJngnjSGGTGzgJGrrbCqLHrCLwSFqV
    hnRnJrwMHnQRRRwMhRrnJRBLZtBPdPSdtZZjjstsLHBP
    pBBzzcbTbzCcFzTvlSCZLCtSvvttdZ
    FWmNzBFNDnRqNRDM
    lvzlSPRDSpDJmNwNGgFpfsFN
    WBHrtrLBbhWHtdrFwFFsjzwfFjsdsF
    HWnrHnTWhWTCTzTBnRZqDDDCqZcclRvSJR
    qcdVbpcMFQcdMGcMFjjpbCnCGmCnJHDHDGJChzCJCJ
    wWwlSfBRgRNBDPJnCgCrgzms
    tBTfTRwlwRLNwTRmWtNwQFZtqcjcdcqpbdQMqMdd
    jJRzwDdwFdJddcjjFCFvQLvNlBhSBhCC
    gMMpbHpmnHpWfnlCSBQCPmPllQSs
    nHWHVfTGdTSjZzDT
    sCVTsBPltgDSbLvRMMDT
    WNrjNmWZwmZwfVLvLRbRNvMp
    mrHGrmnjqVzjGqdmCltlhFFllgsHBPCg
    wbglgTFRblnMRltJBNrDDWQbtdbN
    dqCpcjPLGfHZQBJNGGvQDZ
    SPssjVSqPCLpScfCgFRVmTlRwndlRwTR
    hWMWMhTTDgWMzGMszQShGWJPCQmPVCNPffPfVJftqNQC
    nbnHHwLrnswwPVCPZV
    nsRvFHLsvplvrcsSWDDDddjjDlMTjT
    HRCnhWZhCddgwRcwhdWZHHQLJzSLsSzjJwSjztsmtzsmLF
    VlVVNNVqTlMVbTVVMqvqvrDTjQfFtjstJmzftfzsSbtStJQb
    pQvvPpBqNBccPWWhcWWc
    glJTndVWCTDDVFvnVndVlCvwmBmqBBwQmwPwmMPggGsPGm
    NhcSrhNZHhZzRtHcNRrSMSMSFBMGmsQQGLMPPF
    hcpRzbcHjRhRbZRzZRztzRpJVdCdnfCnlFvdDTnJCVDjWd
    dqWvjjBdWWqMjdvvMJjWWjMGgcfchhzrhwbrwShwgzSqNb
    mmmmsVTlVlsLQmnpsNcfgfwNLgczhcGhzw
    RslpsnGGGlWdCZFMMRWR
    pfSpZSrdSMVDVVLMMDDZpdgRWMPGqqPGqmqqGGhGHNRN
    wwQBwtjvvJlvsnbTbvBRHgPmlqhlqWWHqZZgNm
    JnQwJvjTtzTZwJnbzzfCrfLCfdfdddfD
    PZcnljZFTVmQdlQh
    JCLLqBBCSNCCqzJNzStBpStBmsdhVTdmVWsWmdhfLhWVdfrR
    BpMzzqSzppMMqpJqqgzPFcjZjnTjgcjbPjPDPZ
    HLHWmqBHHqWbMHFtbgWcdhspPcPchndPpn
    ZRVSQZRfVZGRgnfsdhLgLndL
    jzJRwJRSJJvSlZQGRVwlSGZDMqmmCCCmtNbCmtCMzFNLbF
    fDhlBhhZmQRRfHwLdjHFFWQQjQ
    ZMTssVVzvbCqJddsLwwH
    TzgTbgGGzztMbbvzvVbGvSPBrlShRrfnNrnBDlNcRDZRZB
    bjfqGfvFfcHvRwGFRHjbgQtddlQljWpWnQgdWWll
    TSPVVSVwSzSDTDlntDndnlWddt
    hTrrNCBCVrCrrVshThHHbZvFGscwbfFGRJcZ
    jNhMjcgvMNgWggvttcFtchvPFFzdPPrQrrGGQQszRPQrRG
    wSCqCmmppbTwPnGHrQdrGCCD
    LlqwGmGScLcVthVt
    dGGrWWDqmCnwCCQMQrMbFHbMHsPFgPVZbgFPgg
    tLRTBwfvTBJcZFNFZRHSHPVN
    zjJLTvjcwDzqqhGD
    DzrWszFFrtBBhnhNCClHlnHbSbmlSn
    ZLwLcVVZcLVpvRwLgMLpLJgPmmQSNSTNbtTvQmCHQCClCHCt
    cfgZJwwVLJZPPVpRwzhzrrfGGBrGtDrBsf
    BTsdCQsQnwwdcCqqdCnsFvGFpFBvGzrLLmmzpvfG
    pjZjgPVlJVMVtgJSWLPvfFFFbNNmNbvbmL
    ggVplgJtHSSggdCHsscChhdnnR
    JddZcSlvvGFJNWVWFgQgVtFWhg
    nDqsHqCszwjCCPMnfhfBhtdWggfg
    bqppHqqHHbPLjLCppbwDdRNvdmcTmlNZGGbTTcNv
    lcZDSvztcHHcMSZVHVvMZBqBNNFNhBNTTmssBqBcFB
    bbGJQdQPpGfQJQdJfLFhmznhzLLNTFhNhf
    JGpWpPJddbPpPwpjbtZSzltDgWDSgvtrVv
    lJcNlNjPcmtFzHtHBJFg
    VPZdwGGWVrsdPWhWwhzzCzzFppDFMBtttFCr
    wshsLVfWTnGswdhwcmRjRmScqTcjlPNq
    BsBshRZQQsVdsZQZSdsPDwJDHNNHBztNNFMGGwMF
    nSTpLjcvHppzwwNt
    jcqncjjvfmgrCfvqrnZbmdmSsdbVWlsmhRdW
    FjjqRZjZFZWFqPvNvvPQpmbPDCmt
    GSnHSnrScncHhrtncGshVbmTmbpVvppCmpCTmTCmCC
    rHfnddwdfctlzFFgwqlj
    wBwlBmmhwRVThVBmFHnvHMnfsmFfHq
    jzwGSGJNZCCssHfsCPfv
    GJbzbZdbwJdtdQphRVWQ
    ztFZccVHFWHHLSDBpSBPhhZw
    fmjsCmqnNTJnvbTvLRPDlCpBSPBpRShl
    JqsGTSNJGqvqvNvttGQQGdFrQctrrF
    mNZqjTFrZqrTNTTGSSSbrhrhRFRHcnLCzcCdHcLBdccHLzRC
    JDDfJswJDWsvgVgwpWnRBlBzGCnlCLBlcCDl
    vVGpfQWJpsPQMVgvppVwgWPJrqSZTrZqhTQTrhjbjqSSjZqt
    fMSDzDHzpDDVsStdDgwwFZFrrMPCNngCZP
    WmLnbWmvvWTTLWWQWRGQvLvrPFZJcgJFCZNrCgGCFCPNPF
    QLhlqhljWvRQbbqlqnfStnHdBqVVfzpq
    qWZtSQTSvJJvBfJVBBVFNDNHbbdRVPdpNFRF
    fGcwwmCgsLhgwLchbpHdrrrphdPRPh
    gMjllmcjwsLMgcwlMnSWzSqjSZJqZqZfjQWv
    cdRHPjRFRdFVHGcFfFTHQTHVLSQBsbsqSCLqllBJqqbSqLJC
    WwWppWgtNrnzzWDmrrmNvWJClJSSbqLJJCGbsCgJbLLg
    MnvWpmwvWnvtGPjVRcHRRHMj
    dLMDhdHGrcLTvvvstB
    nPqgGGNPqRgRSjgmlWjbbTTPPBvpvpTcswwsTF
    NRGmgjRnWgnZJqllmSqjnqSDhQzVrdQHDfDrDJQDMHDJzM
    RgmcPmGNQwwNmSRwPPgfmrBlCDlGbvFFvtrCsvlrBB
    nfdVnjHdMWnTqflFtbjrllrlsCDl
    TZJVMhnfMnVMHnpRRNRzgJRzSzgwPR
    nTbsblzlnGllmsNnbDwbcWQWwWBFJBQcWQvPWFJM
    HRZRdSRdCdLdRftrHHZfSQBMWJMgMzQPFWMJCFWgvQ
    HVzrLrrjRjLGnlTnlDlsjj
    RQdTdZhWqlZhTdWTqblhNmTMVnnrQsnnpvgMVMHMgHMrgP
    fjSSfjcCzGNBjCjsnpMVpnpzvpzsHp
    GGwLGSccwjwLwBcGLGSqZWmmdLZLWThmRNZmWR
    ncmBrmfdfcVcfGnQdVWRBRvgqNvDvWqzLRqg
    PlpPbSbLFPLpFstsbqRqZMqWZqMWSDRzvq
    sjtjHCwJwlHfdfjccmmLLQ
    tGMtLHQGWzLHFVQtVfQtMWtbgrZNbdfSbccggrcTjTrfbm
    swRChnwqhBCCCmSjcNSdcd
    vnlnqDRlsRnJJqswJvGMFVMMdWtpVJFMQQFL
    ffcHLzGmfvqqfWfF
    rJrrRSPCPMCrPRQMNNWJQjJCnqFVVVTStqVnSBdqppBVtpqd
    jNQQPjQMCRQwwQbRQbjMgwbNhwWZHHmzmlchlHHHDcLmWzhD
    JzvrRHHJvCRZFPFnPgsQVVQNzQTQDttVsB
    GSMjpqdbGNppBtDhPN
    MbmqMwGbZPmPHJmv
    ggVSVWzCNbbNCbRM
    DfVhVsVQcQDmpmQTTQLjPjMjbDGNlbRMlNGG
    sftTFJJccnphcfncTsfBvwZvrzgzZzZvBrVnzq
    qqlClBNSCNSRQMvdrwFvnBrr
    GDfhDtszhhsThpTDzsfpprMrFvQvrnnfvQrwHFbfFH
    VtggWhpsmGVTGJWMjWjNjPWjLL
    NSnShnjsswSsRPNsrnwcwMHfFwGqbvqCbFfg
    LDJtzzmgVVlvqGbzvFfzff
    mlJmQQpmJpZpBJJdjBNhrnnRdTTgNN
    RtRRvbhDFPHHlhtPhRvPRtqjmzqzzwLjHQHLLLQVmVjq
    WNNBBZfgfWnqVQwNqzmQcm
    MZGfzSrTTnWrrWsTWnfSGbGllPCbGlJRvlPllCtt
    phgcNfqgfpZsjjpdsS
    brHHnWPbDPDbTPlDJJvJJPrMVVzRSdFzwdZZzFnsdzzVdVZs
    SbStvJMDQgNmmtgg
    lzDMDhfFRlfMFTfMGPMbFTlMpBHrmpjjCFjmBmrqBjtCmjjq
    VvVswLLHZnJJwdndNsSSNBpCpjqjmNgrmCNtmmqm
    nHcZWcSVsScTDlPPlTPb
    DFMQSlMDpSpFDtDFccHvmqzvbHZjJmvzmmQH
    CSgffrPssdgqRbqzzRqZ
    dsdsfGsrBPsTVcMctMVNNhtStW
    MDWRDWpgDvWpNptvNMnJCHJHMwHCndJfZn
    blcrqTFmmcbhLTTTmlBZbjBJdBfBnCbQBdfn
    TzlnhcnzTmhLRvSDsRzDPzWR
    nwmmPnnPDjclhhjfFzzzwqbFFNHwVqVq
    vSQrbpWCvMWQQWQMLgFJVHqzBzJVNqzBFL
    CvCvQtWWQmcbtntPcb
    qzvtzCCtLsLLzmQCHqpSsHSmfrnNrTrNWWMNGnWZTMZGBvWn
    PglVFJwPhbcghVTBpBnWWhWnTMhB
    FgjcJccVpwDcwFgVDsqzddqLqSDSCQQL
    PLHsSVGGPvSLTffjMJWJJBjfBL
    gNhwgTqDcqwpDqNhFpDFhFWfnndjcMJjMWzzBtntJBnW
    DFhhQRRwbCbwRQChppmTHCVGTlslrsvrrSVZ
    hHnRfSMmsSVrFvQqrmDPgr
    jjcBzjZLpWccJLczBjZjWGwCnNzDvrgPCgQvFggvqrQFnr
    jtZGpLwjWBpBWcfMTttfbHRnHTbT
    swSHffFTVrJlHFSWrTpMZMPhMNBqBhTvhhqZ
    bQbLQGGjQLdRCcQjGZBzpzBZqMBBBVdBqh
    RjQDQGmgmVVbVcjjmjgcnWtJfrwsfrtHlwswnfHSFw
    bVHbbMFDcbDbcmbbHVRbMRFgzSmdzSSTBtTjBdQTzlSldQjT
    nqpqCMwJffqQSzTBQlqd
    wvJhnnfrfsJrCNffvspRrPPFMbPMRRPPMZgZHR
    FnJZnssHvMdJWJpW
    mlDlllGrSGmttwlGvZgrzZNWgggzTzdT
    bCltZCthtRcbcFbjFL
    JGmHrJwGzzpllRZdzZLRcW
    htbPSbVtFbbgjhffgPSfTVSDcHDDDLWscRdsZjdZcRssWc
    vhfFbFvVHbtTVgFPhmJBCvBNrqBBrGCqQB
    BcNQcvcBchSQNccLLvhTqbJZTrHrrrzzqTZMZMFZ
    tpHDsnDRslllCCHtwnpsfjRgrJrVMfMmMrFrzVJFzrVZbZrr
    tnDpjCwPLNSPdhPH
    qnjvvBwBhSSFPgDQLVVDqgLr
    WbszTbHTbsbHJWHLLMVZmJPQDrvmZL
    ctlRbtCWWtvlSfjljjhBnBNB
    TsnvssDDQlRbzMzjDMqHwq
    tSGjZCfFZtCFLtVGWGZFbcqwHbhWdbzbwHbdwbdw
    ZGBFJFZVBLjStZPBBZLRvlmsPTvsgrrnrTllvs
    PgQdNsQFsdNwWqQBsQrTrTLpbrnTpGngnbTG
    hCzzMJVDmfzmBDMCfSfhTMGcnpnTcjGrpjbMGjbL
    CVVfvCflSHNHvPdBHW
    DwlMjMNjStgmthMghg
    PTlpHnJJTcZvTTbHZWZTvpqdHhmLgrgdfrhLLsmshmsLts
    vncqTbPqcpCnbCPvccZbDBCGzzNBwjDlVVGFjjVR
    QsdGGCztZVRddPgndf
    DrNNBbwNHNwlbjFbbCNjNwDWWDPPVfDMgfmVMfnMWmmP
    rvJBbbBNcLCtJZQL
    NfLlqLhbNPddLPqLhpgHwFFwFHHTwRHWwPFTrT
    MSMSCnjBnBjCscjVDVljTvHmmWnrwTrwFTrvTWTT
    JCMMBzDMJcZZCjDzSBDNJgdfdQlqlLNdhgGLhp
""".trimIndent()
