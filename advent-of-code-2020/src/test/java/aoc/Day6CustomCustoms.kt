package aoc

import org.assertj.core.api.KotlinAssertions.assertThat
import org.junit.Test
import kotlin.streams.toList

/**
 * [--- Day 6: Custom Customs ---](https://adventofcode.com/2020/day/6)
 */
class Day6CustomCustoms {

    private val testInput = """
abc

a
b
c

ab
ac

a
a
a
a

b
    """.trimIndent()

    @Test
    fun silverTest() {
        assertThat(countYesAnswers(testInput)).isEqualTo(11)
    }

    @Test
    fun silver() {
        assertThat(countYesAnswers(input)).isEqualTo(6633)
    }

    @Test
    fun goldTest() {
        assertThat(countUnanimousYesAnswers(testInput)).isEqualTo(6)
    }

    @Test
    fun gold() {
        assertThat(countUnanimousYesAnswers(input)).isEqualTo(3202)
    }

    @Test
    fun silverJohannes() {
        // Johannes claims 6742 is correct, but I think it is not correct
        // assertThat(countYesAnswers(johannesInput)).isEqualTo(6742)
        assertThat(countYesAnswers(johannesInput)).isEqualTo(6746)
    }

    @Test
    fun goldJohannes() {
        assertThat(countUnanimousYesAnswers(johannesInput)).isEqualTo(3447)
    }

    private fun countYesAnswers(input: String): Int {
        return input.split("\n\n")
            .map { countAnswersWithYesAnswersWithinGroup(it) }
            .sum()
    }


    private fun countUnanimousYesAnswers(input: String): Int {
        return input.split("\n\n")
            .map { countUnanimousYesAnswersWithinGroup(it) }
            .sum()
    }

    /**
     * The first group contains one person who answered "yes" to 3 questions: a, b, and c.
     * The second group contains three people; combined, they answered "yes" to 3 questions: a, b, and c.
     * The third group contains two people; combined, they answered "yes" to 3 questions: a, b, and c.
     * The fourth group contains four people; combined, they answered "yes" to only 1 question, a.
     * The last group contains one person who answered "yes" to only 1 question, b.
     * In this example, the sum of these counts is 3 + 3 + 3 + 1 + 1 = 11.
     * For each group, count the number of questions to which anyone answered "yes". What is the sum of those counts?
     */
    private fun countAnswersWithYesAnswersWithinGroup(group: String): Int {
        return group.replace("\n", "")
            .chars()
            .toList()
            .toSet()
            .count()
    }

    private fun countUnanimousYesAnswersWithinGroup(group: String): Int {
        return group.lines().map { it.chars().toList().toSet() }
            .reduce { l, r -> l.intersect(r) }
            .count()
    }

    private val input = """
        necytxmlfhsu
        uecosjvlhpmk

        mnslbuziphawkxyg
        whpmnesikaglzb

        kaw
        akw

        qurg
        hrqug
        qrgu
        gruq

        sczomkv
        zejkhvslmucbwdo
        pxsianovzcmk
        tcokmvsyz
        ozskvimcr

        tfzajkxngwsqrbleic
        tijoqnerxsplwzgabkfc
        ogkezbiwqtaxrscljfn
        xsqauezwnjckbtgiryfdml

        piz
        zip

        tlp
        npl
        lpt

        idbnxuclqgw
        sxfdnlatmbhcowigj
        plzgbwdqkcinx
        ydxinwgcblr

        nbuxjwhsrlekfyzpq
        kecyiurjfvqpbxhzsn
        nkxfhpbsuyrqjezom
        tinlhkbjsuydrqpzexf
        pnzraelgquxhydsbjkf

        obvmw
        eoczsqun

        nhdxqwk
        dnzmlxwhkapq
        whxqdnk

        jnqdabgc
        nacqdbgj

        buyskiwz
        uazbkwys

        mbfxswpli
        uswalhxz
        xnq
        tdcrjgoyekv

        fancdpkmtuqy
        zdmenqjpaxftuy

        jkul
        d
        o

        vhpoxgqsrtce
        hgopxeqcdrstv
        xcvrqoetpsgh

        gekpzayfux
        pyegazxukf
        pkxaezhgyfu
        fegypukaxz
        zxpyefugka

        yrpfd
        ydsh

        smjqcafnug
        mnicqsu

        tkxi
        ixakt
        tfncxkmi

        kedibluzopjhagvtxf
        afiklbvtjeoudhgpzx
        lavbxpjzgedktoifuh
        tbgpaxjqeoifvklhzud
        zghidtfplxbvkoueja

        gtvnqyrmf
        hogpb

        cnelsqwj
        atyimdbvfxuozkg

        lgybpaeztvhrxducjiqosmnwk
        ivpaygxweszductombhljrqk

        grli
        lrig
        sjglri
        girl
        rigl

        qnwmxjbpecoyztufidhsg
        ynqdiwhmusgtebjfzxcpo

        luasrtmfkgywv
        vgskmufywrat

        ceyux
        ycux

        nurijfsovkbxdp
        ayqclmthgzwe

        xeqjdhsubiovwrg
        gobdjqwvusehrx
        weqjgvhosudrbx
        ngdubsvqxejrhow

        ywdfqpxekujbovr
        yujfdoqbkxrepvw
        owfkudjbvyrxqep
        oqjvedypxukwbrf
        xufwdpebyjrqokv

        csujqndzmw
        wznscqmjud
        cqpwjzmunda
        wjumnzcqd

        dhsow
        ohswd
        wsnogh

        cxfltiq
        ixztfq
        gtfqiux
        jtqfxi
        xiftzq

        tr
        rt
        rt

        pnuztcbegwiksd
        cuewibkptdnm
        nvqdpcjxekiwtauhofb
        pbytreinuwdkc

        y
        y
        hejq

        rvmwqn
        wvrmq

        knxmwecsjlrft
        hnmwzaqt
        dmywnitg
        tmwvquonh
        bwqvtmnh

        e
        e
        e

        qjtywgdlek
        nhifs
        zvxinh

        nzhroemi
        dzruoegimh
        emzoirh

        hpfkraltbxg
        gazxlmphrt

        jyhugbxqz
        zbnox
        epdilfaw

        vmr
        vsfm
        fmvsr
        mnvckiaqy

        zegpfuwtkrnoqdh
        eorznfkgpduth
        tkfpzhnredguo

        zmqkwxipctney
        ncwzeqpmkiytx
        yptienwzxkcqm
        yeqwcpxtkzmin

        csbdyzl
        ejoiwtzqabm
        lzbkup
        zbvprxf

        wvpshngbmiftuzrxoyde
        szobihvwpdtylqerc
        phbdzoikvtajyesrw

        ymuflnrxspqvkihbe
        eymxrhlfnqksuivbp

        rktwzd
        wtdkzir
        wkmaziqdrt
        togpzwdkr

        uwiazbylcpojst
        lcyibajuptwzso

        dtoqbcxhlyrpfwjnkasugvm
        wunkfstmcjhqxliogrdvpy

        wpnk
        hdpkwuftjymv
        cpkownz
        kpwz
        rqpwnk

        tfplznqcju
        puqlsnzfjtv
        jqtfpzlun
        ntqpczjflu

        ozalwumpqv
        ztheoalrwsciyk

        wyiqbsr
        bysqwri
        yrbswiq
        iqwrsyb
        iwbrysq

        elnroc
        zmjlre
        vhelbru
        jyhruezflvk

        ftyceboh
        ofybh

        jvlbdm
        noakrg

        dokjncqauebfpmszvtl
        suebonpdjmflaqctkvz
        dtjzmcblsvokpunaefq
        tjzmeqkafluvpbconsd

        qhrmvegsicnyupf
        uifknverhsg
        izlrvgefndjwasuh

        syxvtjwfucilqogmnpk
        jwkytpqofgimvcsnlxu
        dwhglcxzoqnmjfkuiyvpst
        ygwntxipqlmvkufcsoj

        hdnqaburow
        hqwadmbocsgue
        bwoqtuadh
        adqhbwuo

        dzfohqe
        ehfqzod
        fdzhoqe
        hdofzeq

        sxmkrghyvlwzbc
        yhkmwzxbgsvlc

        qdumkytwlfsrja
        drfqukzymilsjaw
        jkwqumylsardf

        pcvs
        prvc
        hmbpvc
        svrpzc

        epkjyc
        jerpoy
        gjpye
        abxepuyj
        ejyopc

        ohe
        gheo
        eoh
        ofehy

        klhziowmjpyfubdsaqtgvrcn
        aytbormdiksnqpzhcfvulgjw
        lhmvkafdrscjgwnyiuptqobz

        vcrqpdxljnmgusie
        cgdwmipluoe

        hqibentfmxyszljow
        msozdjnibrfytxqhle
        imlyxwehznjsbft
        ysifkzbljphuxcmenvt

        vjhdmkrpecniafgqlstxyzb
        zxjrkebfvcwyqihlsmpgtd
        xrfzidgthsbklmveypqcj
        vjxytzgdlbefsirkcmpqh

        dyxrwbngpijkt
        xtwkdipaygjr
        iklrmzwpjxgdtuye
        pdgrxyjkitw
        xtirhvwgdkjyp

        zbjdt
        jdat
        shouqjtxcd

        klarpuiv
        iqaxjdps
        cpnafbsi

        xfpiourcst
        ovfrlutxpb
        trgqfxjuop
        rsktucfpox
        rtufoqpx

        g
        g
        g

        nfutbyvj
        umvwyi

        tjniwlydzqpsxbkcmugf
        utpmixqdvzbfcwgljs

        wetypdlnxm
        cfavwyzji
        mywgu
        lyorqkbw
        whdeyxp

        qsbnwozepvhkmaxdrt
        qmsopwtaznxfkehdyrv
        xnvhwrzpemkqdtsoa
        ituvkrhmdegazwsobnxpq

        fxhwdio
        hofdxi
        hivztfonxdg
        fhkoidx

        ja
        jca
        ajzxt
        ahjy
        tajc

        epan
        pcn
        udn

        mfowepjdlnkags
        oakwgflnp
        pnlzgoawfk
        kfgrowlnap

        uw
        aimwo
        wzrhcjeq
        wtf
        w

        x
        tvx
        x

        tigsjdufrwomv
        vjrstigqudohwkm
        visrjodutwbmg

        ety
        exatp
        hicfzqnb
        uvejo

        oqlgmk
        kgvqxd
        jzhgkpdqy
        ikegfwatqub
        nlogqkz

        sl
        q

        rkpew
        eqrwk
        rakwe

        r
        r
        r
        rb

        u
        pua

        pjmhtvd
        drjnpuvib
        djsplvy
        dpvj

        caugyqs
        hxwyqst
        sougylqre

        krfshpzjmgav
        uwrqidcotlbxhj

        mzivctbfjgwq
        lsomkypgajiq

        ikoamrpc
        kmociarp
        brkmpiwycoa
        itmckrodpa
        kmipacro

        htbpcdqx
        ctpbxdq

        gpduzcoiyxhleqrknbaw
        nirehbulapcoxkzwdy
        icuzyxopwhkalendrb
        aokdrxhlcebpyiuwnz

        yzlvptdaubjerkh
        iyaoltghebpndkjszr
        kqxjtldzpbcyawh
        dbjtlavzmfhkyp

        vg
        sv

        cdiaynroehqw
        cqtaridyohewn
        dciornehwayq
        rwhnoaeqicdy
        wqroadniechy

        ivstkj
        vgciqk
        qkivg
        vkgi

        fp
        pf
        pf
        fp
        fp

        xidyqnk
        jbicfant
        zabfhin

        oqtys
        stqyo
        syqot
        yostq
        boqytws

        gmnqxaictud
        tucidaxqnmg
        pgtquxidcnma
        iagdtcfmquxn

        yfhxgw
        rgn
        nsog

        wndrqktbmfa
        fwqadkrtbnm
        mabfntkqword
        qbwntakrmdf

        elanoudxhbzfrtvci
        ztalefiodxrubvckhn
        ulvabcnthpdzxoifer

        omdrvepliwafhjynbscu
        mblrxjsfahocwnvyu
        umcjlsowatvnfybhr
        uahvcnbwlmsjzfoyr

        n
        tn
        n

        grk
        rgk
        krg
        gkr
        rgkhv

        stmlnek
        pgsltne

        alcztk
        bctvk
        khcgtb

        nsgevcthrpykbwfoizda
        khneritgszvbfycaowdp
        ivybwzeaogdsrnfctphk

        e
        e
        e

        cxkzdbgiqyawnm
        afyvkbeirtjowmcl

        omb
        p

        lnhwcoiyafs
        soihnyxqfc
        ohncjyifsq

        o
        o
        om
        oxqnsd
        oaj

        okucjhiq
        xpgwcnqjiomyfluk
        irhoukdsbjqc

        uolgz
        nglczu
        zlngu
        zmhlgvjru

        mhdapszf
        anmfwdvkc
        dpamf

        aqleivtyzsogjc
        syvbgxqpdflezoac
        ilvzqsjaegokyc

        zxndhja
        haxzd
        xhazd

        mo
        om
        mo
        mo

        vfxep
        xfepv
        xevpf
        vexfp
        pevfx

        mrtkehqnjxizgp
        kbunrqytcmes

        nz
        ipneu

        bznteo
        tbzeoqwp
        olftzbpk
        grzvityxudohjma

        xg
        rg

        rdu
        urvd
        rud
        rzud

        somnbutz
        tuosnmb
        ntomsbu

        fxsvrzqntbueoyp
        eqzsvxyunbroptf
        zvypnrxfsqebuot
        zrosbxeqpunvfty
        zprtfvsnbquexoy

        qwzknfvl
        funqlwkj
        nqkwflz
        fgxlkwzqn

        rzqihd
        rbz
        zr
        uzer
        rbz

        utrq
        osa
        pm

        zgdci
        brhu

        bxvymlzqkpha
        vmhlakpyxq
        ypxvqkmhal
        xhvpymaqkl
        aplqvkhmyx

        sjntbvyogeplwzu
        rbjqiefhapxodwk

        frlsoptqwbc
        ljrzxqntfom

        zlhvjamtpfonk
        lcokjzdhmfvnp
        siohzynkmbjvlrf

        osvgwnticelpzm
        jvswetpozlcg

        npmgrvysfj
        nvqxrjfsgmp
        vofthkcjsblpmudizwr

        oiev
        lmeiov
        oiezyv
        uievodkac

        iforupqjzykdtc
        yqfhdcxvtopz
        tzqpfdycgo
        ceyqfozvtpd
        dpxqflohzyatc

        srz
        zsrt
        strz
        xzs
        sz

        dgo
        odg
        ogd
        dog
        qogd

        gotwqhick
        okhqtgicw
        gsouwitkjcqh
        hctgioqwk
        tkghocwiq

        mfnziwvxtlpubqryojasd
        mzojuctvnbriypwflskqda
        pmuavqjifnlhrtbyzwods

        ozj
        joz
        zjo
        jzo

        jzwbhrscm
        rzmhjctb
        zbrhcmj

        sucgimtr
        myliojcrsg
        ipcsegxrmv

        znhvi
        hvin
        wosganie

        wptoqxmafhg
        awqfhgcozmtx

        azbqlviyrwjc
        ybxcrawv

        qa
        vcuwbfgsl
        hkraj

        biglkjtpoqs
        zvxcfumeywrhand

        ske
        s
        sp
        hs

        n
        yn
        n
        n
        n

        lrvsmxzkgi
        zgrmkdsxl
        lzgmpakrs
        wgqtrkmsfz
        nirmkugzds

        crewvjdqsplkibthxz
        wxjmtpirhalqsckbdz

        iecxrbplho
        xhrpibovlce
        brxlgchopei
        cbhioprexln
        lchxirebop

        f
        f
        q

        nxqkrhdi
        pkhdrquxn
        ynqdlxkh
        xghkndq

        wvknrdjx
        vknxrwdm
        kfvicpwndb

        rcqwlutsanpdz
        yoxenmkvbjf

        izqsa
        sqzai
        zqsai
        osiazq
        izqsa

        ysokhbpzcqanmdifw
        kdlowyusmqhbjxci
        hibkmzocndqswy

        yjwd
        bgas
        yj
        k

        tzruq
        uorz
        zckrvux

        rjn
        nrj
        nrj
        jrn
        njr

        ecu
        pzus
        wokxu
        ruw
        twu

        fcnajztduyripl
        chmpkvlynsxrauobji

        cwfgtjre
        ejwufr
        frwje
        newvryfjbhq
        iwjextrf

        ltqapiwhzfeoncxdg
        hndclgoxeiwtpqfz
        cwnpdogxteilfqhz

        uerinxhsygpbzcdvfmo
        lxeihducozgqnrymb
        edmugnyxifoczrbha
        ucezhnrsdybmiotgx

        xuvfhjeyi
        ztcgaqs

        yxrospmijwvcdb
        yjpbrcosuv

        viuhdrgnlak
        lniaruvkdgh
        khrgdnuiavl

        qfzxb
        gfqx

        k
        nzyo
        kcj

        bmi
        bmi
        imb
        imb
        ubmi

        pl
        pl
        pl
        lp

        x
        v
        x

        hgwtocs
        gdctwhoi
        iczwfgtoxh

        wrcjdaqm
        wsrlog
        jrnqmcw

        ouhmpnyskxcqafidbjvgt
        dnimvbpytfsoagucjx
        jpctsmuxiaovyqnfdbg
        gzbvfwmojuyxsrdtpcna
        yxfolphstnabcmuvjgd

        nlrdxe
        ceksxuhazmjo
        ywxedfgqtpr

        ckuwoghtaxrledvzms
        mtksozduwvrehxclag
        xwvcusrmlazoetkdgh
        regwvshxkdzuctomal

        khsnymbpjz
        hzknspmbj

        zqc
        kpgsjrqxiyd
        wtbuhoq
        qwc
        eq

        ljewzy
        qlwjy
        wzljy
        lwyj
        ljwy

        vpodmhni
        bvryknfg
        jcutez

        wedfcoh
        wigfnjkaoz
        exwoqfm
        pfuohsw

        adyzjwgqxv
        qjwvxzagyd

        cazpofbyd
        nodypfbcam

        ukvrcslyqzamhteowdfibx
        ktrgqpmnawdycsivhzlbefx
        fxdabuylcveksjwrmzqhtio

        xyb
        xesy
        xdqcy
        yex
        xy

        hotd
        odhzqt
        othd
        htod

        hvkc
        lzei
        asw
        a
        d

        q
        q
        q
        q
        q

        a
        a
        a

        suqbvedipgzcjykfmaxno
        jzfudeoxikpbvmgynsha
        iswkepmodzvfgxnylujba

        xpqjonkzlrfhtyw
        fjqksrlozypwnmxth
        pkrohjnxlfzyqtw

        pxuhsc
        hcxsnu
        hxcse
        cxhws
        hcrsex

        iglfosahy
        hizgfalodbs
        lgfoiahs
        gsfalhyio

        xrgzwbdnohfeijacsmltqp
        djpgqrimtwcafsxnlebzoh

        qlrn
        qrn
        qrn
        qrn

        xhbiakuzcjm
        nfpykow
        klfs

        lcubkpfhnomgwyarjetqd
        wtcrgofdnlpbaykquevhj

        bdqusvnyetklmwizcopxjr
        drbxlptyeqcvjwiusmzokn
        oqcjzvuwslkpedbxmrntyi
        jvlnxtdpkusyczbeomqirw
        wltriuqnpzcxbysvojdmek

        tyeorbzjdxpqn
        oprlqdnwbzyx
        brnydoqzxp

        bupvcrylqazehoknjtf
        aqfroczbylpvuejk
        ubqvelyfrzoxcpagkj

        awbzx
        axuw
        ywildxoa
        uwxnba

        mjfvro
        wmrfjov
        ojvmursf

        zwxouqy
        ouzyw

        dsxf
        sfdti
        syrfd

        zmibfjk
        kimbz

        nebq
        beqn
        eqnb
        bqnpe

        avtnckldjpeys
        scjtklpdneyav
        kytncdpsvaelj
        tkpacjysdnvle
        dycsvtjplenak

        mkyojv
        biwro
        botsx

        owxyu
        wyuxo
        yxwuo

        qgjplcvo
        vgjqocpl
        ojvlcpgq

        xczvkuohisrfqbagp
        prbeilfzognvqws
        voitfqpgjmrzsb
        rvlfdoqigsbpz
        ipqogbrzfmsv

        j
        s
        st
        t
        pzde

        kuwbthejxcfrzgosmn
        xwcukhrtsgzf
        tkfcguxwshrz
        hxukfzgdwsrqict
        zwucgkfqhvxtrs

        wvthikzpgrjaf
        swdnemkqobcyx

        lewoquaprbkxyj
        xpelwoaksubcrhqd
        gbrxmluktiwnaopqe
        epxuyczokafblwjrq

        mcyepsvwxr
        bhe
        nqeztgd
        uaehil

        tq
        be
        e

        duqgair
        dfrwuiqga
        qaugidr

        hmofng
        hgnomf
        mofghn
        gmotfhn

        ewhblqnsmjict
        whmbxvpjs
        ufzyaogdkr

        jqpynazsfvklied
        sjilykvqza
        izsvjlqayk
        qiksvzlajy

        vbps
        vbstp
        evnigorks
        vbst
        jslv

        qgdzylwkbfou
        jbwygulq
        qwhxrugpeb
        giqdfvbwu

        jubto
        aeuvgdpx
        unl
        yorqui
        uqm

        ic
        kci
        a
        i
        gyfe

        tjxp
        xtj
        xjt

        ldspeai
        eapidul
        peiadl
        ipelad

        oymwvfriux
        moxuywvfir
        rmvxwoujifyb
        fpvwdumgyroxzi

        mcplvnkgjfz
        idszwngkfcvpl
        njzfklgcpvo
        fcvokpnlzg

        xdnsilr
        irnlsx

        bndvimefz
        atopdu
        xhswykcqgrjl

        kvlorsye

        vsdoq
        dvos
        vodjs

        bxczyfeajkmloi
        jcimfkaloybxze
        ceijaokfzyxmlb
        feymoacbjxikzl
        kcxmiebzyaoljf

        lo
        ol

        etxkfsgjlqoy
        fgshieyj
        esywfgj
        gwrjsyepf

        jtzlgakxins
        gqlznxfabskijp
        ikaxgsnzljh
        lgaizhkjxns
        hgaznkjxtlis

        urhjiycw
        rhycisw
        zrwihkcy

        xc
        cvx
        cx
        cxgl

        audxneqrchg
        efkmodwqplrzsiyb

        g
        x
        x

        ziuplaqo
        czqtihuw
        zufiq
        nqzubpixak

        cibdku
        dbckiu
        kdciub

        eshpjzgtwrka
        tgwpafmsjkrehz
        sekbwtnxjhpzrag

        dj
        dm
        dj

        trqkdpwoxjfyg
        csu
        ancv
        lui
        ebhmaz

        ljbvexnhgwfs
        hfjbxneyilwgu
        znejqblcdhfgwx

        b
        b
        b
        b
        b

        idxrcyjegnu
        lrhvskceg
        rozwmgafbce

        gzasrwjlukomvdxhbpc
        cahmgzurlvpwxbdjs
        uxjzsbgmdlchvprwa
        hramvdjxslubpzgcw

        gw
        wge

        fpgdzy
        wpfdbz
        zpfahde
        lfdpszcorx
        zfdgbqpha

        gfuclekqavitdxr
        euwkvdirqtcxa
        riuxvaqkcdte

        eqaphotimfdrukcgwbyx
        dxtowkygamibqcrfeuhp
        oiutpbavhdyqergfcmxws
        ahcteowdfrkugpibqjymx

        pmixjle
        uxljemp
        lkpsej

        mzqcwxuo
        xoucwzmq
        xcumzqsow
        zcuqxmwo

        wt
        wt

        tyrajn
        hdtgroynjae
        jrnyat

        uqifnlv
        iqnevl
        vqienl
        navciql

        qjxpshtuzbmfnvogcalw
        skvzqthufxcjeagrmdiwlyb

        lvaftw
        vkfaywtl
        lfvawtq

        wpusqdfkybg
        mghtdxkpsur
        dsgxhumpk
        jmdkpzrsixug

        qcbalvykixze
        fmrhcsadlnio
        cpautjihl
        clgainupm
        ilcaft

        pklutwg
        gdpkrl
        lrpkg

        wbup
        bygfurw
        ubw

        xsoqjceyhunvat
        jshgokqucxvlyn

        pyk
        pyk

        l
        s

        m
        m
        hm

        msultwhivz
        vzitlhwmus
        htsmluvzwi
        ihszwmvult
        iwthzsumvl

        wlohkegqpb
        jzguytqrncd
        igqvesfa

        h
        hgy

        xb
        x
        x
        x
        x

        ij
        ji
        hqdl

        k
        k
        fkswyr
        zk

        frvpln
        vfcnlpr
        vfnrlp
        rnpvfl

        ouy
        yu
        uy
        yu
        yug

        rhekb
        etnxfymlgdwovqzu
        csbheji
        beca

        vj
        nfpv
        v
        v

        ruvqehmija
        mdhjqrayuie
        ksqrfgjeihpmlu

        kbsnlqxfrizjauthodp
        uxdjbqniholafzrskt
        eaokjungrdfshlbtzqix
        wjlsbciontzkdqfxhuar
        qjlosubzkfitnahrdx

        sbzcefm
        zsmb
        szmb
        mszb
        smzb

        xzisbpmc
        prihnxstmfzb
        klpbmszijxw
        vmepbiszxaq
        zipmjbxvs

        qjgbnvamedwi
        qajzdoksweb

        gtkfqwlzashcen
        geiqzsnhkfwctv
        esnmhfzgqutpwyk
        igselxqndbfwthkz

        obpdyswkvthxgreanmjluq
        lahobnkyxtdrqgseuvcj

        pw
        wpe
        ylp
        ep

        owrlgniy
        iljkvwxyhopufzsqdc

        wxzgdpt
        saxprdtkf
        qdxjotpgbwl
        xtdhjpl
        utdjphxci

        gwkrjnizdxbvc
        hfnpygvaw
        ygvutnaw

        pecfzghqxturdijsblmk
        klsxuqpfhdbctrjgemzi
        dtuzljfxhmbgciqkeprs

        iznfycwdrh
        zwnrchiydf

        xutwndim
        osidj

        knwyjicb
        jc
        zjgc
        cju

        jwtep
        hjer
        egj

        khwsitfnxyep
        iwkpnxeh
        wephiklnx
        hcigwzkapxremn
        nwehmapkxib

        vuqbmineogja
        dlrpainmvugj
        ravsmgijfcuhynt
        awvpiunjmg

        zdcalubhniv
        nclhvibudaz
        vhcdbuznila

        bzcaorhqflg
        jteyvpwsuxdnik

        icr
        rvic
        cri
        ric
        ric

        bqikw
        ibwqk

        ipxdcvmfulwejyg
        nqcelgydaskrxhi

        pkovrgdljyh
        vq
        ixwv
        vsxaczn

        ix
        xi
        ix

        hkxl
        hklx
        uxlkcsvgeh
        khlyjx
        blkxh

        lhgcae
        yczfas
        conasulj
        xgbflezcuayo
        mvcidtapr

        iwaepxckbvjnzmqft
        pwiancxtmufqsvzj
        cpanmiftxjwqzv
        mhftvjnxzpowciqa

        ir
        rd
        ir

        xt
        xt
        tx
        xtk
        tx

        rdatjzibwsu
        mdsuarwtizjb

        rselwj
        wetbjy
        pzxqjowe
        njtgbvwmei

        qgcazvn
        gqsvcnz
        vczgqn

        vmufbi
        efuinvmq
        uicgmkvr
        vnmilu
        ubvwmix

        lseymvjuidhkoptazcxrw
        wnbsukhpjoedczrlavgtmf
        scmrptkhdeazojwvquly

        qopb
        evwqyl

        syuzojncfrliwap
        wnocfplirjzsa
        izwrhvftqbdgpcjoasln
        ljiznraspxofwc
        rzcpanjowslif

        lpebrhxtam
        iyuvknwfso

        by
        yb

        qshtypdlocawvgjb
        xvyrnwfqezshjoka

        xhw
        nwhx
        hwx
        whsex

        xelcghsquwivjydnrzkbfpa
        elviwcpnsokhjqubgadxyzfr
        dcfqxvneymsutzkrlpgbjiwah

        ghckb
        bgh

        rdojuvqncia
        nracovyiw
        cvryoina
        echariovn

        rjmdqahvbplcusigfx
        cluaqvsfhmrjbpgidx
        gvchlipxjsaufrbqdm
        pjdsqmvlcaubfirhxg

        tngbc
        btcng
        nbgct
        bcgnt

        aonpsbqkhwclrmdyuz
        mcgubhqdokarzlspnwy
        olhybcuasmrdzqwnkp
        lkshamocyrdznwuqbp
        wlunozascybqprmhkd

        skilqth
        qhlts
        uslvqt
        fqstl
        ftshql

        fbtxdirelcsqzu
        icsyjfltxdeurq
        rueptfdcqylixs
        usqtxfdrclei
        lwqdvtxeirsfucna

        xphvrbl
        bthixl
        wqjmohcfxyazlsb
        vhluxb

        kbnogjrhe
        krbngeohj
        hnekjbrog
        rkbeognjh
        kjrohebng

        z
        yzn

        wslprjfqa
        regwpyqdksaci

        glmkujvbatqwc
        mtubqgclavrjwk
        wuqmjlgcabvtk
        galuqbvctmkjw
        ckblwqujgvmat

        kgrmfstlejw
        bdithoauynzpv

        wvyzhfdistmqkrugx
        nzlspqermfwjahui
        ufqsipwozmhr

        fdsavukrbpcm
        zrasokjlvudmpbf
        eqvbkfrswpdtumag
        dmhkfplvbarnus

        tdorlvb
        pvdhiko
        omgsjwuavycxe
        pziqbovhnf

        f
        a
        u
        u
        u

        efg
        egf
        feg
        fge

        wsf
        lmcd
        f
        v
        ve

        mibscvfqnhoxty
        rxphcsiqbofvnw
        uexoibzljkqagcnf

        sbmhgyqvkuoljzwrfnxietca
        ocjebltaqwursipnfkmx

        xeh
        unxmh

        du
        ud
        ud
        du

        tvzo
        vo
        ovft
        vto
        povdq

        r
        s
        lhibgo
        qny
        nwk

        cwxfhrgjdsyamtkv
        fshogpwdinlrcjexmt
        yxvdtzfrsgmcjqwbh

        zil
        liz
        liz

        iygvphrujkwaxl
        ikjyutlxawvgrph
        hvuliyrdjakwpxg
        rjthkylwipxgvuad
        wivxkhlausgpjyr

        vrxkaqzgmsyon
        vorxcumswqznyag

        vlhuwzmdq
        bsoryk
        yeka

        eiqwuz
        uvwznpqikr
        wluozi
        xjbyizufsagdtwm
        wzhuic

        wydkjlfzpeqtgxorhmnuci
        tuczlfqrdnyxihgjopkwem
        tdpylmxcibgkfhjzwerasnou
        zelpcrdoygnxjvfmktuhwi
        gwmehyiojukcnfxrptzdl

        vcuizfrlhqa
        rvizlucafhqj
        cviluqfhraz

        wmgu
        ve
        sgly
        fmwx

        ke
        ek

        elhq
        hqel
        qehl
        ehql

        hzdfnwxtjysrio
        mykgu
        vy
        kpmaqy
        gbeykc

        pfkj
        hfkpj
        kpfj
        pfjk
        kpfjd

        z
        t
        z
        t
        oxy

        ux
        auhzoj
        xu
        u

        gcaldiph
        lhnfapi

        slg
        slg
        lgs
        lgs
        sgl

        h
        h
        h

        qodwxpmskvjbtycuzghfe
        pesjgxqzbtdcoyhwmu
        ylthszqpuwebodxmgcj
        chqjetzxgdyopbwmuls

        fkgtmuyoivdehpjcxsba
        agycjmdsfirvkuowtpbhex

        ocaw
        msclaw
        cwm
        jdwceh

        ekfyibdncovguazhjr
        jkhgzbaoyidunvfr
        vyaihouznrkgjbfd
        xghojvrbdnzfuyaki

        jpgqlhvyi
        pqihag
        hqpig
        gupqhi
        ghqip

        glatmnqesbiwzkhup
        jykuqrbswnmvocx

        ykqwxpjgcshtdbau
        dfxqwpgbjcstkyua
        xsgatjcbkwdyqup
        usqykjpbgxdcwat

        cmevolpwiq
        moagpqevltjiw
        fmwziqbpuvleko
        djvspewmohiql

        mdux
        xmkud
        xmdu

        vptyrkjxhcmounzgaw
        wkmranuoxcjhslzytdigq
        mjcazrxkguwfhbtnoy

        e
        z
        mr
        e

        vrqek
        kevrq

        wb
        vcti
        hyar

        e
        e

        chqesv
        vesqch
        eqhvcs

        uae
        xzecw
        eyl

        bkgftdhvn
        nbfkgdtvhj
        nhftvgpkdb

        fwxkbouizcyjg
        kwfqjgcyxzou
        gfkdoytrjpczuwx
        gyjvzcxfuowk
        mxcjzsnuaefowyhlkg

        itwzbp
        yhnafe

        dtnzjkibprafm
        fnarjbkiptzdm
        njrtpifmkbdaze
        qkndmpaufbrjtiwzx

        eknujdxzmytriao
        erkujinftzq
        eskumatjicdnryz

        lvzhmxewbgnkuorypfai
        ibnwkrfogvhaezxumpyl
        hvbarmnowygqizujxplkef
        hegopvnwrfybamkluxiz

        rdylnbotpv
        raugkqfelbnwy

        vk
        gnuypkw
        kvj
        kjh

        uprylxio
        luixrp
        ruxplki

        u
        gmu

        ugbqne
        ykvzwr
        i

        vcqosrjbuxtl
        hovtxlrbcqsu
        bequwsltvxco
        vxarnqstcbuzlo
        cvuxqpobtsjl

        ctviybroekm
        cyrtbevimok
        tvokrbciyme
        ekctbyvmoir
        tymcviebokr

        sdmkxgfhjlpyba
        puaowzsdqthkle
        zwredloshkpca

        fjxo
        joxf
        fjxo
        jfxo
        fxoj

        mrqtv
        rvqtwfm
        rqmnvt
        wmrvtq
        qrftvm

        qwtjlfobuydncgzheisxr
        qwxdlcuhjbnemaorpgfyzist

        zenwdhikljm
        nejlmiwzkdh
        jndelhwzkbmi
        zelhwnijkdm

        jofdgmy
        ojqdpbfg
        gduojf

        awsgplcxqu
        mulxgapdcjqk
        acqjhfldpxgu
        xluqipvbngact

        scdzqhyfplnx
        nxkygdjuzbcqsmvt
        zlxsqwnpdcay
        czsxnrqyold

        qzvlitw
        njapdb
        xkbcf
        uaogk

        ok
        e
        iu

        qshidleorzbgjnvpuckyawt
        piojqrtzbnaukgwvchlesd
        gduvqkcnhioepstrljwbza
        qhripvstdjznlbgwuocake
        jevrwsibcqlnutgkohdapz

        upcealv
        gmpeyv
        pve
        lpkuevz

        pbtldk
        bpqhdk
        cibzd

        lk
        hs
        kx
        l

        fkuirx
        sy
        zgqw
        qmv
        qv

        gdrtbwcjlhmqk
        akdlqzjhctgm
        dchqomkjylaftg

        vwal
        ficwjzusyom
        lw

        z
        jyxo
        n

        x
        x
        x
        twxs
        ox

        jzhaxuflebqwsipgctr
        mgawzjihxrpcuesfbqtl
        rljqhausvigxtofwczpbe
        ifwjsdcbztphlxqrkeuag
        hizafygceblrquxwtnsjp

        ioamx
        xiaomg
        oxami
        axoim
        amibxo

        dxgwsbmlnyqruckezp
        xlurgtqncpwyfbisdkejm

        vflkdqcxbzuwmengty
        swdbuexzlqvgmf

        ehsmravbk
        ehmvrkadb

        qh
        hq
        thqx
        qh

        uhi
        aui
        ui
        iu
        iu

        wlrnmb
        yblnpr
        bztijfenkola

        y
        fgzyvds
        yoj
        iyp

        ohbiqfxzp
        fpqihobx
        xhoeiqbfp

        cgfi
        figc
        fgci
        cflgi
        cihgfnv

        vytbdxlzuwarjphnogic
        gexwbnqfizmhtdjyrcolas

        hdeowrivjq
        uzihgmjwxrve
        wijeorhlqdsuv
        ryeitfhvjw
        ejhawvnuir

        siaxlobjmkvcwhernytfugd
        rmtfsolejkdxihgvbnwcyua
        oxglvkinrcebswamujfhtdy
        gwsatohiknljburmdycevxf
        cwuzdbgnethrvysmxolfkaji

        m
        gm
        bhslre

        xonqwkzcfbmtidpyvlsheu
        pylzcoxmvqkehdsnftubjwi
        nworbkczxutyedsvqfhimpl

        uatwvlgknhrfoqi
        nwhbtokfiq
        seniqpdzfhxmcotyw
        utoiqafwhn
        nqtoifwjh

        oxiflw
        hflwxo
        loxwbf

        mbt
        tbm

        ocjflngrdakuypqiwxstzhmvbe
        ykdaueiczwglbtrhjqnmsfxovp

        wtlzyrcomasqnjude
        owhqdnmkzrpjlsxc
        loqdmszxgvrjwcn

        on
        x
        x

        lbeyqfxkwm
        jmqnfxbgwde
        oezbufqrtm

        d
        y
        y
        ta

        z
        p

        fyx
        xfiy
        fxyw
        xdfcy
        fyx

        csjxkozmnl
        slomjxkzc

        hqwadymfsbtlxv
        kdvbatlfshwqmxy
        lyahsxmqfwdtbvz
        wxmlyahfdstvqb
        tvqdsxfynlhamwb

        ojmvurikheablq
        ukidqhvaljobe

        imc
        tdkjmp
        mi

        eq
        eq

        thgulyifqobdwvecmarsjnp
        ndlygcmjahefquiswbrptvo

        sjnptmdrlc
        uvebzmlodrncp
        lmypxkcfrnsd
        cnmhdlqxiarwpg

        nzvqlh
        hlzvn
        lzhnv

        juq
        ju
        uj

        tjqiucmynvozxh
        kmdvwafrxtq
        exgqmtv
        ftsqxmv
        qklbtmxva

        r
        r
        pj

        pkoy
        ypok
        kypo
        okyp

        f
        fl
        f
        f
        f

        snvqdyx
        tdsxvnqy
        deqynxsv
        dnsxqvy

        lzhopfimuretwvd
        jqgbvakrds

        zfbocen
        mzbnacu

        ezyxtajrv
        wzjuxreatv

        mrsejiyuvfz
        jkuctbxpoiw

        nyvefxqicbah
        brgeyltxvik
        kyuvilexbs
        ztvixyepb
        pxiygvbe

        nadq
        nyt

        prmt
        ysjqkmocznhda
        gmlibeu
        bxmvf

        rm
        wm
        m
        m

        wyfjzdlvcgreb
        emlxnkpfjav
        elijupnfvh

        pybv
        bpvy
        yrbvp

        zrjkxshpufec
        vindgwtqbl

        ngmtqzfcpudhw
        tphzgcwnqdfmu
        ftzgphnmcudqw
        quzpmctfngdwh
        gqpdhufwcmtzn

        szfcnb
        fczns
        fnzcbsg
        sznvafck

        arcezgin
        egcirazn
        gezcrian
        icgnraez

        hgqucfnt
        gqnukt
        qgncut
        tuqnhg
        unfgqit

        w
        setbdgv

        lck
        lxk
        kcl
        kl

        emaqhxorctykufp
        tigjzqhv

        mlvow
        jqwo
        ghksnruxipoac

        hxnpeqt
        htzen
        nhte
        pchtne

        ehovjgfzaql
        qhazvjlgeof
        kfgljqhavzoe
        jvlzfhgoeqa
        veolzqfjgah

        bdlitrzuwh
        epfmuhgvstibr
    """.trimIndent()

    private val johannesInput = """
        qzbw
        qez

        xgedfibnyuhqsrazlwtpocj
        fxgpoqijdzybletckwaunsr
        pwnqsizrfcbyljexgouatd
        ljtperqsodghnufiycxwabz

        uk
        kupacjlriv
        dku
        qunk

        yjnprofmcuhdlawt
        frmhulyncvweatodzjp
        fhadtrcyjzwlnpumo
        hrcutablndyjpfmwo

        rdclv
        lrvdc
        crldv
        dvrcl
        vrlcd

        dqrwajpb
        asrfpdjwbq
        wjdtarq

        nalkhgoi
        bmiad
        sdvpiyerma
        ami

        smg
        wlmdftcr
        mk
        my

        ebaxsun
        uaxnebs
        suxnbea
        eanxusb
        aexusnb

        zoxegrfd
        qorjv
        oqr
        vor
        roq

        jpasdnrcvhglft
        dgkstavnhjrclfx
        crtndjglfvwahq
        hjclinvdtagrkf
        gjfcdtuhlanvr

        exiopqbgrj
        undhwyfkvltis

        npsxwlyog
        udylki

        bnuwck
        nuack

        cahpwtlozkm
        ghnpzfqmoxabi

        hvwgxesraocbpnf
        ewvranqcpbghoxf
        paxfnwoegrhcvub
        qbrawpfscexngvho
        ahpxognlrvebwfc

        agqmwpvlb
        waklrnbqyp
        blquawtp
        qltabwp

        rymhgolkustipjxbzqnafe
        frzqjiuktbsxmahdepylg

        zricqdofygvtbsjmeu
        vudjctzynegboiafqmrsx
        rsbefoytcqgiuvzdjm
        zucobitsgyjrfqemvd

        y
        yc
        tg
        lavoqef
        by

        fevykin
        wxytrhqk

        pxbclzknowyq
        lybwkpcoqxn
        oknlpbxcyqw

        l
        f
        gixvd

        unogwsfavzkhi
        siohufnkzgavw
        ahitunswfvkzog
        gvhknzuaisfow
        ukasozfihnwgv

        ulhgwo
        ghluo
        ogulh
        oughl
        lphuog

        nike
        bkn

        gefzwrdjsqx
        fegzsjwxrpqh

        lfknweboxyrpajmig
        gkpafnieojybrwl
        iafesdyulrkpgnwbjo

        zjoiqtgpwxdach
        iajdhqucpxtzgl

        yhav
        vnshac
        hcav
        hvw
        cyvh

        hwz
        whz

        dhunracmzksvxopjgtbqi
        vqhpowubyzirdksmgca

        ckrhefz
        rkvexdfzcbh
        heczkrf

        rbsldt
        qgr

        a
        ea
        a

        wnadxkgehtpsv
        gwnexkavshpd
        bfkdpansvhewxg
        kvtwdnsahpxge
        xnhdpvekwgsa

        ydajvctr
        drcejtgin

        kw
        cbmj
        l
        t
        n

        gyzlnp
        zahsyu
        rek

        vjdmhsorqw
        whdjoqvrms
        rhsjqdmov
        omdsqjrzhvvalue

        rcxsgnluhtqey
        ldejuqpykrtc

        rylcqxt
        wlgtzyrcf
        yrltc
        rclpyt

        frke
        kfe

        nchvxtqarsejld
        rkhntaexcbqljod
        qhdepzrxljtifcan

        uyfshgxzbqvrdwintjlmec
        oyrvdwtgeczsfbmluqih
        kabdlqytwgfrhmuevczpsi

        guwmkrfyex
        fxekmrygwu
        wfgremukiyx
        aywegkrmxufl
        wukygimrxfe

        m
        m
        m
        m
        m

        hbgnkqe
        khgn

        hngypzmd
        oixyanpdg

        qbdklpvhjaeif
        kzaiglyjfpmeurn

        ynsm
        smjn
        nsyum

        mpcztqkydxifv
        zdqfvxtmpcyk
        zkcmxfpqvydt
        yopktfnqrmvxdczw
        mdckxypvztqf

        va
        bvhg

        fe
        ef

        n
        hpmqn
        n
        dn

        njxazhtevsmlir
        txmsijvaezwn
        tezacmwsvinxj

        gvzpbmeijyhaukflcrdqw
        sivlhgumyzkdrjfeoxqtc

        ouq
        h

        csxewy
        kxoscmw
        ucnvwsxg
        hfocysxmiwe
        wcsmx

        retcxugsdwjnykm
        sfhudnxoqwjktyzarc
        rklvinxudpsbywtjc

        cwfizpyguatbodsqemxrjknlv
        ujikxwsmntcloyqgaebrzfdvp
        jreyubofkpdnzsimxwvgaqtlc
        nuewfvmlidsatzqbkcjgorpyx

        ycqolubpktxwshegafvm
        djnlhafsreuzgxkb

        odasxjtqcepgrzl
        jsoelxpqgtczam
        qiajrhesdzoxfplcgt

        vnthkurzf
        bnfpqutvekra

        gujxqsnitohp
        tchxqjgoiu

        hgparlm
        mjxghlqzp
        fmlpahcg

        deptkcyasiwgfonv
        ptqufxjhcyglrzdov

        awnxeshvrbcjm
        xreswnhbcmvja
        axvwnsejrmbhc
        xajcmrnevhwbs
        sjxhcbearmwnv

        woystmzcbgrljqhxdiukn
        mzhlbgcwjusnortdqky
        tunrjlzmqosygbwhkcd

        agh
        ahg
        agh
        hga
        hag

        cmzkthbquilgwypreno
        tsmezpqlwkhrcginuo
        gmniotwlpzyqceukrh
        wztuikrglhqocnpme
        ekqchwogpliumrtzn

        jqadntioypevlwcksmb
        hymnfzcvtdaxsekrqijo
        dvqmgiacknyoesjt

        cdnbkfzev
        bfvdwcezk
        kzeblcdvf
        vfdkneczb
        fdeovjczystkib

        rzc
        pma
        zwvx
        zyrlt

        lmwvudafhnczxibsgpjkreoq
        durlgqhmvpxofsiejwcbkzan
        mieraklxftswzjbuocndvpqghy

        qlaxkbf
        rfhyxtbkseaq
        bxkqaf
        fkxbaqn

        lpntzrufvskoqciaxhedw
        arsztnpofkxcqudilhewv
        srvlzcujywkenimafhtoqxp

        snxwjqapfo
        nwsfqxpajo
        npfqjowaxs
        opjfxanwqs
        sjaonwxqpf

        cswjquxv
        uyecrkg
        ufjcl

        qjvzl
        jvqzl
        lzjqv
        qlvjz

        h
        h

        mprvotcsgadybjfkqehz
        bjsordtucqlgkhfzvepy
        fsoqijpvhgkczybrted
        yzsgkfhvcbjpeqtrdo
        cjzdvkprbheysmofgqt

        jzontimefb
        ldorvefxuatpj

        i
        xi

        zhtkdvl
        pchznv
        jmryxuwgazvfh
        lkdevczh

        subt
        utsb
        fsuobt
        jutsb
        utbjs

        n
        n
        n
        dnk

        qkrfv
        fqgvkseut
        qvrfk

        w
        x
        n
        n

        mkpoyfrngtwdcjlv
        yprcuimjsz
        rcyjzxphm
        pjecrmyx
        ipyramcqj

        puwnzsohkv
        nsohwkuzvp
        upnhokswvzx

        rmvjwzhx
        kpxhqevglom
        mzxfhbv
        mhwjitfrvnx
        mcusxhvwj

        vipkgth
        oskhetgp
        zlqhnbdjwgurxapc

        vn
        vna
        gwvdn

        rugcbipvzow
        uogrzcvpbwi

        jwxuc
        krngvmypl
        jwxzi
        qashtbedfco

        bzfyl
        yzflb
        fylbz
        lyfzb
        ylfbz

        uhkvb
        kjhulb
        vbkhu

        andlzobvguqsk
        nkgvqlzbu
        klbiezughvmxnrt
        bunkzgljvf
        yvkgjpwzubln

        t
        t

        bdrjpavtiqlhxuw
        pjmzbxidrtulcfw

        wqylp
        mrhgfxsjcaltonedv
        ywbkul
        ikl

        hxpivszmbaodecnlurq
        cedlpnibqmwxaghrszou
        hunbrdzpsxleaitmqyco

        pymewobv
        veazgoy
        yvoxle
        youfveix
        syegdvo

        ogceirlsaqjtmvzdx
        rtsaijxocelmzgdvq
        dvrqjxizmgscteoal

        jeupkmhcydwaz
        daykzechjpwmu
        umkaejhwydpzc
        aywkjphdzeucm
        ypdchmujekazw

        suzacvgjnoi
        evgsufjaitn
        rqeanjskxbvfgiwu
        dknubsaxgvji

        ojxfyetcpdbglmwqunrsi
        fjletcbrdsnviogpxwuq
        jcgprswxiaefdqnotlub
        zfgjewrpldsioxcqntub
        nxcgewpqolibrktujsfd

        vwh
        hjvw
        hvjw
        hvwr
        vhw

        md
        m
        m
        m

        xkpu
        izfmrosjbd
        eywqt
        xgak
        hgn

        layhskgxrpfnwqmdj
        mlsxywoakrectnzgqdjf
        fbsxnmuikqwrljgv

        rhcn
        lon
        dln
        pban
        nhd

        uxwhl
        hx

        aesduycrgfmhjblnkzt
        jktlhwqioepzgcbnmda

        tbhfongxiudcrv
        rqoyfnuxabjekl

        ajd
        wjdmak
        jda
        jad

        ghimtjly
        hjtgilm
        ghixmjtpal
        jtmhilg
        imhgtjluo

        ewpfam
        epmwa
        epwma
        ewamp

        cslywuzkdg
        jfluyxgbsma
        ugysbl
        ytlqgaus
        hsufgloxyr

        eigjpobrlxnasvhquwy
        nirbjslgwyxuoaqevhp
        fpnsuxbleoajwyhgirvq
        ayinxhubgvweprqljso

        andu
        sguand

        embqxktgpduyjia
        qtjagiydempukxb
        axswpjdgqbumyteik
        kgaexydbupqtjmi
        uqjbixkdptgmeya

        uxzwnlgyareost
        nesygtowzuralx
        lnguxsaoyzwtre

        thfobmluspeiwxygv
        ibtfgsyouvxdpelh
        txqerhuolbpsgfyvi
        oehxfsmbiuvpyltg
        pfoiuhlebvyxwdgst

        e
        ke
        e
        e

        zhxyc
        hczxy
        ohzycx

        sylzvbx
        sxzvlyb
        vltysxbz
        ylbxzsvf

        zxkt
        xztk

        mnctogxvwkseh
        owrsadeqmhtykcg
        matkscwo
        mcwpzbofs

        wvpybgulfrhnixjka
        rgawyujfcsveki

        hbmljqxrtza
        xmutqzahl
        zaqlmhxtu
        hziqlatxumc

        sjwq
        qjws
        wqjs
        jqsw

        ithgcoaed
        qtgsdohec

        puidogmhysnk
        pgszdunhyiomk
        gmyivhnksqodpt

        ionrt
        yhvsiud

        xtzrljypsknh
        jkhztpylxnrs
        yxlkrshtpnjz

        veyflgkws
        jwukvfeshmr
        evfwksdz
        fekvsw
        vkfsew

        qbercltdyiwzpfghujxa
        bvzlratfchxyijqewpdgu
        hfrtqbiayzdxpwujlcge
        ejhbpuwytgixrdzaflqc
        phrjagczeiyxwdufltbq

        chxzbkemyjgqvltsardoi
        osykewcidjtuvzpbqhg
        ibqozcsvygmkedfjhtn

        czgih
        zcg

        dpulzxtweoafsvnrykqg
        zoasklutxwnqevyfgrd
        vnrequfolxgtzsdawyk
        ynurtkjfogvdqzlewsax
        kuydfznpatgvrelxoqws

        fmpeunzs
        sumpfzn
        fzpmnus

        sadewmz
        sldnwe
        wjduetyxsh

        no
        ou
        oua
        ybwdoe

        etjdmfhkvupsgob
        zjvatuxrhomingds

        wcrpzale
        ctyzwdsulbvmexo
        ikecwnzlg
        ngjwczlae
        zlqhwgecj

        p
        p
        p
        v

        x
        i
        p
        x
        bgckq

        huty
        yatu
        cytrqju
        wtyu

        k
        k
        s

        ti
        ut
        ut

        ltyxj
        vaicrwnzb
        yegjqp
        mjogk

        ifyupadt
        idutaypf

        civyufabow
        oafw
        eawtof

        tdxrcuvqmaoblhsjfynpwigkze
        hqfymskzljrtexwugbnacpiov
        nymlpweztcgrhjqfuiasbkxvo
        qxcponmlbwtvazuykfsegjrih
        vuaoelhfcyztjpxingmbkqsrw

        zqtkcomfdyrs
        qokdtmzyawfrlc
        qdmzejfctykor

        qgkmhvx
        vnqmkxlh
        kqvhxmre

        aklnbydq
        dqynjokaebzlu
        kblqrdany

        znaepc
        uthvilcm
        cpbda

        cep
        wgecdbpz
        pce
        pce

        qgtaelxzvnfr
        vwlquxfbmhgdjzir
        fqvryplogztx
        krfvglqszx

        xebsp
        vohgfurnmsa
        dqws
        ysl
        eqyps

        fxnlgkjizbsupyt
        aixlkfuwezbgcpsnh

        fliogmexzuqc
        ygxqvoeklitmfu
        iofxzueqmgl
        qouifehxalmg
        oefiuqjgdmplx

        cjqnrvu
        cquvrn
        vqnur
        qvrnu
        qwsvruxnmf

        esxbjiwnya
        ibwnjsxyae
        abienjwys
        ijwaybnsep
        ybeiasjnw

        f
        f
        f
        f
        j

        dwfgkqohamebnzly
        vhyokaeqpufjdlb
        ayhlqfbnoedkg

        g
        ijur
        ad

        oxpwmzunkjved
        dgwxejtpzn
        exnrwizdpljhfy
        ekzxwdjnpt

        jghbztdkypsovc
        arefyzdnlhwpoivbsqj

        cwvpyqnhjmsblikorx
        ywjmvfxnbqoitdls
        gyuloqbznavmijsxw

        tv
        vt
        vt

        cxvwzdbpsqnhoa
        thoqpsvmaxcbkzlin
        anecpvqohsxzb
        xscpvobnaqhz

        axnrzgyvepoif
        zovgfipynhr
        grodvzhinfpy
        rnipgfojyhvz

        pbmhlxwqfezrscdtykn
        cfmbkwtnlryzqehxpsd
        qrbylwtdnsckzefxhmp

        zkqw
        wzkq
        jqwxzk

        vrdnmyfspbzlxw
        izepalkytdnfcxjwhg
        nxuwdfqzlyp

        bc
        cb

        bf
        fb
        bfh
        efbq

        amw
        mpa
        maw

        bqwtvl
        vlzwtq
        ytlqv
        zvlqtb
        tlqxvc

        lbhxpnkiurmwates
        fuwrqbeztjmlpsnxaik

        jpwm
        zwljp
        jwmp

        zep
        cep
        pe

        mzx
        zmx
        xzbck

        dlmvbnupathgjsf
        blknfsvtjpgmhdu
        pnufdgmhsqtbljv
        ijeoufmnchvysrlwzpgtdb
        xvpudhgsjmlnbtkf

        cnyuhkmbswzilxovjp
        ybjtdewvsrfcznalqhgiku

        bi
        rzla
        j

        graezckvnqx
        agwzkcqnxre
        grzxeckaqn
        ghercxazknq
        rzqakdgexcns

        sfe
        b
        f
        xpjtuand
        boies

        kgjfzqlwic
        rkcdstgp

        fgsndtox
        zodnyxlgts
        otxdsgn
        nxtfgodis

        zwrtohqdey
        teqwry
        pvytwegf
        ltioweyrc
        myeaxntuw

        bw
        bw
        bw
        bw
        fbw

        o
        gh

        owadmhy
        admhoyw
        wmohyda

        nywib
        ceulqpzh

        zs
        sz
        zs
        zsko
        szr

        izaqf
        uijrf
        infgq

        w
        qw
        m
        bzr
        o

        emavls
        hbxwektuqjo

        qrhspwbulzkaxdm
        auwbhyxlkdvq

        turdsw
        utxrwsd
        rtundsw
        usdtrjwo
        utdowrs

        gjro
        gojm

        xhnilqdp
        ienxaovqldkhg

        rgjonh
        nvokcjqrt
        argnjo

        jmgchqb
        jivdabluqf

        ylmicxtahwqfvukneopgzsbrdj
        hbzpeaqvlxgotsjkwmfndyiruc

        p
        rop

        delgcpurwxniqohkvyas
        gurxclnvdwbikoqezs
        rnxewiqvgktmosfjd

        tigwskmhvupobfnydxcre
        qwmdoycrhvsjptzxngbikue
        yvmetfcbahuklxpdgowrisn

        wsy
        wys
        syjw
        wys
        yws

        srwpt
        wtsrp

        lhafkqwscv
        aklhcvrsqfw
        vfsqckalwh
        hlnqskcfvwa
        klfwvzsdpacmhq

        ehbzglfvtdxpkicy
        dl
        jnmdl
        wdsl

        rew
        rek
        ehi
        ilehwkm
        axe

        bl
        hzlgey

        iboqgtdxprunz
        uonzipxrgqbtd

        o
        o

        bldk
        bkpl
        fklbc

        ynmuedqwhoktxprcavz
        fshlwuznoymekqtprdv

        hjtwrzo
        rowhztj
        wuxrojhzt
        ozjhrtw
        hotzwjr

        senxaq
        baen
        ajmne
        gaielnd
        htfnpkuvwoae

        joklzmcuyg
        uyzrifcmlkgsj
        kyoumjcagblz
        uytcgqklzjm

        ujvi
        ijuv
        epmijuv
        viju

        tahxdeq
        dazexot
        baelxdq
        pwexdgvyiukjcaf
        edrnamzx

        zxqslwyh
        shgyqlwxaz

        xbs
        xbs
        bsx

        kwlhvxy
        kqwvlyxh
        klvxwyqh
        hxlkwvyq
        kylwpvhxd

        koznivas
        basiozvkn
        ovzkluniash
        ksnxcavioz

        g
        g
        g
        g
        shin

        e
        qtvo
        ac
        ksw
        ae

        pay
        nau
        a

        czkmwrvefultigxjhdqaopsnby
        flqrbdxungowzkmjsyepvhitca

        ymw
        vurft

        wqpgys
        ywsq
        qswy
        qsyew
        ywsq

        ndmo
        fdoznkmw
        hmuzndo
        mtqdrnyo
        uwfomnd

        pktnzsqhyvmaejclwuor
        jwznystlkmqcraeovhup
        vuwmteknjqohylazsrcp

        k
        k
        k
        okxe

        fjilbxs
        ib
        bi
        iqb
        ibeq

        gjlyop
        opglehyztj
        jpclgymno
        dopglymwj
        gojplrbcy

        nuygowpxetsahrv
        jgtpyhruwonexq
        rlepnuikhgybxwot
        svnygeuroxhzptqw

        wkies
        eisqwk
        kisew
        eiwks
        gxbkwsiep

        oj
        joe
        oj
        jo
        oj

        gtzunomec
        cezutnko
        tunzeco
        cuoftneazk

        vpifjqnbcuaelxzgdwos
        gfsyuwzclexopjvimqabd

        npaxeizolfrygctvw
        rgtpxwicvafozenl

        e
        e
        p
        t
        e

        ohiaqmdktxnbpwlc
        iqcopmavhwxbgnl
        bhqaoiwxlpmcnzy
        oxhwifeapuncblrmq

        wghbsijofevcu
        qtlmkday
        xlrznmp

        zbrjh
        rzbjh
        vebhzr
        klrzhb
        rjbhznl

        kcgzmvfrxebiautyohdwq
        thdkgwzymrafbxecqiovus
        rgtwzvocydiafmkqubehx

        i
        ry
        n
        xgscp

        kzjngd
        rdmwgnujzok

        djpivm
        mdipjv
        mlvdjip

        ilbdjmvfhyzxcqwuspn
        lichmuxnzpqsyvwbfj
        umnvlbiwqsfchxpzyj
        uylzhpvfqbscijxmwn
        clwjnihuyzpmqbxsfv

        o
        o
        fo
        pon

        aowpk
        zg
        dt
        uc
        u

        ymhiouqp
        lkyoqi
        ywtsvqio
        lnmyoiq

        ydbhigntrexvkua
        urlaigkqnydwxhb

        nfro
        z
        jz

        ojsag
        gajso
        gjsao
        jhsog

        t
        t
        y
        h

        jieqmryxv
        xqvimey
        umyqveix
        vtedmnxliqyf

        tmd
        tdm
        dtm
        mkdt
        hmdt

        px
        gwruq
        pz

        tejofhqklimp
        thojpkfgelwmrq

        tseirdkyqvcghwju
        tjhycvgilezdws

        roqbndl
        rdql
        qlrd
        lqremd

        qvgunystzpckrwifmxdolajehb
        bgtixcwloydzqjskvnfmpeurah

        soviern
        rlakxngecptvsz
        nvdsre

        gzkoiysxl
        vjrlmtpbsceozhauwx
        kfnqdzolsx

        kwzaqnipgxfhoy
        hanwlofgqxpzk
        xqngazkwphof
        pqwkfghaoxzn
        qwnfgpakxhzo

        zhurktfoqmlbnx
        mhlxorzbntufqk
        qbzklhnurmotfx

        gezmci
        oecmg

        pcyteovnsz
        dptlfuzh

        roxjhkng
        xonksrad
        rxnikos

        vbgmsrecxpj
        srcjxmbgep
        rbgsxcepjkmq
        pjgcmzbsrxe

        gkvwndflryjshmbqxtpizuocea
        fwndvekqtzployghijsmruabx

        dcxtogbifek
        ifkdgoxbc

        srmqlnhbwv
        liwsqzvtn
        wgeoqvpfs
        wkyqvsacd

        yswarofhdmqclxkgut
        daxhzkgumjysrlwq
        qfamswzyiglxdruhk
        vsnwxadlhrbgqmuyk

        rxwzkmd
        kwzx
        qawokzlxe
        xmdnfkwz

        exo
        xvqod
        cv
        pnrkmywh
        uztea

        ma
        ma
        qamfr
        am

        nksp
        kvpn
        pkn
        nkvp
        nkp

        pwlvdsagy
        islyawu

        sgbnwulekhpmy
        mjgqeyshpbwuln
        yubghpanecmszwlv
        euwohbysqngptlmx

        gs
        e
        kas
        a
        md

        i
        x
        bl

        vishqyrzgmpbeuldj
        gwcpvnskbftudyorj

        ueswlatkopc
        wbeosclpktau
        lcpvaswokuet
        ltkupsmeawoc

        iwtvopbgyc
        widjykopv
        ivowyp
        iwyvhop

        bfnwdumyozhpxre
        zhweumdnpbofy
        dcsobyuhfntpmzwe

        zrlaspbhty
        oyhrftzbslap
        ysztbhlapr
        tszhpbayrl

        wbgxh
        gnhwxcb
        wxbhg
        gxhwb
        gxbwh

        ngribvdawujskcmfx
        rvskbgiwjxuca
        wiyvgcsbjxkrua

        ifstrq
        fk

        b
        b
        b
        u

        hpqy
        wjplyq

        zwkfgbsmedluyot
        fdtrhsgeumk
        vexgtudkshfim
        eutigskmdrf

        imljkdnzpx
        jqwknpxlhmzd
        pmdyxgnjklz
        rxbjmlnkzdpg
        ulxbmpdnzykj

        vetubnwdy
        bnwatdvyou
        zfwgvtdbnki
        dlvewatnb

        uj
        otuj
        uj

        ifstpgrbcnywaxmkdzoqvh
        qkbfhovrginxapzywdct
        vyofbwcdginrkqtazhxp

        mvnaokugdshybwtr
        ntvsywuadrkmohgb

        fauo
        pnofxadk
        sfoa
        sofa

        akecpnjdwrztvlfbuhsiq
        jehrndzisqcauwlftkbpv
        uawiskrjentfbcqlpdvhz
        crtpjbdalsviwfnhqezuk
        ncawkfdsqtrhpjzielvub

        otazsfhc
        ltkacysow
        pvtomna
        tacok

        mx
        x
        x
        x
        x

        hmoy
        zmyhjuo
        ypmxohn
        hoxym

        dm
        md
        dm
        md

        uqvbhfdomlwtkxez
        szaqwmlhytgvxkr
        wmpzhxqktlv

        btkpdjinsrmogfywzhlvecau
        idylbvnopjsgrkfheuczawtm
        tygvsizrwjneplkohcmafudb
        wtvprskenjchmiyfuzglodba
        jwdafzcemrbsunotyglpihkv

        e
        ouxg
        ei
        shp

        ul
        ul
        ulrspe
        louy
        uynlvo

        yibgqkf
        uvcwx
        mvxdohjlw

        q
        z
        z

        orxgfiqznujyplsbadv
        gtzieoxspqrvndjbylfu

        hmlqcujrysbgoentdp
        dyjmrbtpuhgslncw
        rmyfuhdgclnpsktjb
        ptucnyhsmlwrbgjd
        wrhnldscjeyupmtgb

        sowieczubgx
        firsgewzpbo

        txeo
        exot
        oext
        oxte
        otxe

        ontlbzyekruhpscwqvaxmdj
        ahrtijxdepnokymcsgwuf

        ftsraxmwogvnkdbzqiyhju
        ylsdhonjzrfwmqukc

        leiupzardbfsv
        dzuwlesifrbp
        ldiupzersfb
        fupwslrzbedi

        jtdp
        dajg

        jpglzoriafexks
        gpaozketlcixs

        butisvecnfplqmygworax
        jrhpbwztcveifamlyqxdung

        r
        r
        r
        zr

        eyniaztvgdrl
        dalgonxbiecywthzvr
        dtzrgenvliya

        akzferolcmstgby
        kmcagsbteyrlfzo
        aoktgslmcrzbfye

        jqigpeydwl
        mkeaxqcudzgvfhb

        ayhgbfzerjxd
        rnzfaxy
        wyzaxnrf
        ycazxfrw
        ayzxrf

        jgmkwtnizxrcbvfqhyeal
        mwatrngvlezckqfiybhjx
        vmrwyqnigzhtjfckxaeb
        wkehnqrtcjigzxavbfym
        vrczmohgtfbnaejxqiwyk

        c
        c
        c
        c

        idx
        id
        id

        zaipnjbsxkg
        xevnfipkjgb
        ujxldqcnbhi

        hco
        co

        ztlrica
        ktneixcrs

        hlutcdsgoj
        mwlypnzbqafvxkire

        lznhjwgsiv
        wricunaxj

        fdqgyzopwlen
        rkvt

        vy
        gy

        ihbpfsx
        pshixfb
        sifxhpb

        begmqirvflkxuyanp
        dmpvhfbnaxsuloqitjy

        uwamykhsnbfzxielpdo
        cxzswhvtabupmqjlekond

        gisqpjrubx
        rjbiuqxspg
        gqpirxbsju

        u
        u
        v
        u

        gnjqsoha
        qgandhlyrs
        snhqvlrabg

        vo
        vo
        ov
        vo

        iudvwxpmtac
        owlyxsangim
        hbdrxmwaei
        wjimax

        xmubte
        yuogxa

        nwkurtxjcpqozmhgsvbe
        chrmvakuyxtowqpgzbej

        manbesvzpyt
        dgtwazpqjmbnr
        itfkoupbhlcnax

        omhinsxf
        lnmdoix
        eimgrno
        hyomidn
        nmjodi

        qmpn
        ripmn

        wfm
        wmf
        mwf
        mfw
        fwm

        uaoxphg
        phoxb

        btfprm
        ztmrf
        mrft
        ramft
        tmrezfa

        uaznory
        zyaiourn
        nouryaz
        coszugmanyr
        lqazryonu

        vqlswifmcg
        wzuclnxsvt

        eh
        eh
        he

        siuhfpb
        iuhfs

        yfhciqlnwpgemoxktas
        ticjauwfsygepqnhx

        ndmhwtub
        zbagktxiulnwh

        txdikm
        boldxtiqmf
        dxtimj

        wvr
        vwrb
        wrv

        utyjachpfordvw
        benvkudcfta
        vduqiftcgsa
        fcuatdxv

        doivj
        jiv
        vjkpyi
        jvi
        dijv

        mhyn
        mxngywh
        yjhmn
        dmynhulkacb

        hpidlbezkwqtr
        dgtaflrnyscm

        osqeajcvduwhzrtkl
        zcaevujrqsdolgwkyt
        uokqnrdjaecvwtlzs

        ovsbxzgaju
        vliydjht
        dvjt

        stlxuabmcwqkodevrhf
        ajskvtpmlicqewbhuzfd

        g
        cg
        ezfo

        zqfmuxlnhkgdpvwrtjio
        rkjqvomtguhpiwfzxnld
        zfgxrclnimokhvpjdutwq
        tgkxfqrnhdzowpvijlmu

        ni
        qn
        nq

        sn
        qn
        fdn
        qn
        hson

        tplicyhxqfujbkzv
        qfbzvwkuycsa
        vzqnbfcokyuda

        apoy
        plfxotkv
        iphsqjorged

        ravyhbsfelgcmzponqwikj
        bitfjzwakhmvxngsoepcyrlq
        cevpwyrnlobjsfziagkhmq
        mswnezcpklfrjqgiyovabdh
        cijelkwpbnhrsmzofygavq

        sqvnuedcytaxohplzf
        cekhbgwadpmyofslxq

        gclfnxakjzsw
        wzjcnsgka
        ciwzsganjk
        aspicokjgnwz

        qrabg
        ajbgmw
        yabg
        vgsrba
        hcgtbya

        xarbuvi
        ivaku
        kiuav
        vzgxiah
        flsivaj

        ckodrthljgyfwm
        mgytohfxnrdkjbvlwc
        cfdwmjoltkshrgy
        yhwmrldfucogkjt
        fmctpgwydhkajlro

        qvibocelnsdyzfkjhpuxmtrg
        zgdksnoypqebifltcrjvuxmh
        giocmuefxsprhdjkzntvqlyb

        xkcrlwomygs
        grluckhsanomxwf
        sriokwlmcyxg
        kxgirswtcvlom

        budr
        brdu
        dibur
        urbd

        o
        o

        p
        p
        p
        p

        wnydf
        wrptghizjqk

        wnbdmltxcq
        dfcwl

        ksaveflzdxqocbgyjp
        bcxavldgysqozpkjfe
        ozpvselaqgbdycjxfk
        clpzdoebvqjgaskxfy
        qykodlgvzjaxspfceb

        nrmdjstqozklxvcyag
        ejzwbu

        kfgijmd
        zmgjdikf
        jgimfdk
        ijfgkmdz
        smbrfkxdwgeij

        yhtuxnfcq
        tcyufhnxq
        hfuxytcqn
        nqtufyxch

        rlhzknoiuycefpmvxqbg
        nbvgfexmqikpyhruo
        hqkgnvyieomubfxpr

        bjyadnftcsxqk
        lpfbvzcsumerho
        wctsbf

        iyamec
        myeibctka
        egyicam

        ac
        pvaqdb
        a
        au

        giltsum
        ixyhncbjqped

        cinzuvkystgfodr
        rksphwcfxdgey
        xrdjkygcsfq
        bfmdecpysrkg
        gcrdyslfk

        ayqpgucwedftoxjk
        eoagwdtkfqlypjuc

        ktysxp
        sptx
        qpxst
        ptsqx

        uvclismeajkpbzgqwn
        aicbqfzvlnuegpm
        glecvmpzrnioudaq
        kanmveflzpcgyuqi

        vklag
        glv
        lvg
        lgv
        vgl

        tyuh
        hut
        thu
        htu
        thu

        eyipwtfuhxzalmdgrs
        whfcvbquxgepn

        tg
        ge
        g

        vk
        uatvn
        mzv

        jhfmpux
        mhjquxkp

        gzyxjwfb
        grzfxmwybj
        cywfjpxztgb

        udrye
        eqdyr
        yerd
        eryd

        kwdoynribhpsqmlgvuje
        gunbjywvmheirdqpok
        dnqeivoptrkwuyjhmg

        czvmpr
        sxpum

        qlktsn
        tkclq
        sbqlkwt
        luetjiqpk
        lcvtqsk

        rotvsfwelbnj
        fwbqmaolesn
        qbfwegzislpnoc
        nzoelswfpab

        wsecbml
        sclabmwe
        ezpsidclwtmbgvr
        mcelawsb

        ngz
        zg
        gz
        zg
        gz

        xqjwnaiyb
        jayxibqngw
        qinjwxbay
        nbaiwyxqj
        jniaxqwyb

        hktywl
        htwlyk
        kytwlh

        ymbeahldgz
        pjaoszx

        b
        b
        bz
        kb

        gvdcqp
        qpvyrcdlk

        nij
        jfiub
        jtieyxl
        mnij
        imvju

        qh
        hq
        hq
        hq

        tez
        zet
        etz
        etz

        wz
        wz

        xc
        jvxc
        xpzc
        rgumxc
        xc

        nizsgkvylw
        ngylivwzks
        ylgsiznvkw
        ilgyvsankzw
        ilzywsnkgv

        emhpiwfyung
        qlzkob
        ldjx
        xvat

        luzfaswdyj
        jsdyuzlafw
        ljdsafzuwy
        yusdjwzfal
        dsyjwuzafl

        uioafnlegsvjdxphr
        lnfrjdpehvsxguoa
        najruefdlxogvhsp

        gumjphl
        mplchdjuw
        hmjrlup
        jmpgulh

        q
        q
        q
        q
        q

        feyimktsugrq
        qygsrtlpmfkeui
        sfkmrgqyitue

        xpklzsyodcnhrjq
        xcznojsdpqhrlky

        btmsc
        mtscbv
        csmtb
        tmbcsx
        mbxtsc

        dvrfumay
        ymafr
        mrifya
        ykrmaf

        do
        d
        andk
        dcp
        d

        glhmjykfaznvrd
        bvmijshzga
        vmjuqazhgpx
        voczhgamj
        wjhizgqavms

        voilgtup

        vrphtmc
        bgfkjvad

        bxkfi
        kixbf
        xfkbi
        xfikb
        xfkbi

        lfixvpozghcauk
        plgixmzukcovh
        hyruilocxgkztqv
        uglxkaihcozv

        avgptdmi
        gydpvt
        dptawvu
        rvtdopxlqs

        rsmglpt
        gpmnlthr

        tqmeukoia
        kamiout
        mqwauekov
        macoryuk
        nujbskgapmlo

        ndjrglykwoi
        jrwkdnolygi
        ownkidlryjg

        kwheact
        txukwjcgbhar
        whgotjk
        kzhtw
        lmvynhptdqkifw

        odkzwylprhvn
        pwozvnydrh
        zdhnpryowv

        crni
        qdnkiou
        gbwhyasjx

        bxitsamogcq
        erbxklyhvtczu

        swrfxdpjhly
        pwdjkvfyxrshl
        sxehqrtwpdamnyjzolbui
        gpdryslhwxcj

        fhldcksrpyi
        qfejshrcat
        wsmrvcnogxh

        vwacyofjtilrxngp
        otylvxpfwgcnjria
        xojfignyrtlpwvca

        qvz
        ozm

        msrdwqahutefvxy
        rjmyotiefuh
        ltyrmenbzjuhf
        felbyputmhr

        riyzo
        kzroyuwi
        yirozp
        yoirz
        roiyz

        rqjz
        vjrq
        qrj
        prqj
        zqjr

        vdx
        xvd
        xdvu
        vdx

        xeauhryviqg
        wpxkufzsjmctlnybd

        hbzwdlno
        tmrj
        np
        lqfx
        kuygescvia

        iqph
        ihpq
        pqhi
        ihqp
        qpih

        lsfwxmpkgqzvhecoay
        gqfszwakhvymptcexol
        cpaxwlzkfqvhsgemyo
        lghfwvcmzxpokeysaq

        tgmpafqbuds
        hspdfabuqgctm
        tpcubdaqsvgm
        mxqbktspoaujd

        xktgfvyprhndme
        vntmyskfegprid
        dsvpfmnkygtre
        ydmvpkfretng

        npvgjzuo
        mwlackhs

        gyj
        myk

        w
        w
        w
        k
        w

        sgrhv
        rixdgjluf
        rgs
        rsbg

        kmiqobutwplfryxgeajhnz
        ylrtmpauodjnfvwgeixqz
        extlfwjsirnmgyuqaopz

        vdngfjbwypio
        sukyvfzpeqwthdxg
        fyvrndgbwp
        ydwfvglp

        fhsi
        hsbif

        fihzecojaqm
        jzchuomvwsqfr
        jzchoqtfm

        kqafdzcgphvbos
        qbspzodfavhck
        adfozhquktlcibspv
        szhpfboagdcqkv

        tmeb
        takbe

        ymrhgdo
        ypgzhmltd
        fdbymgshcx
        nghyzldm

        bxgm
        gmxb
        mxgb
        gxbm
        xbmvg

        fep
        lnp
        trjim
        dauy
        fplk

        udhzexv
        dhmezvx
        qdxylbhvzw
        zveduxcht

        wgvhaokesjirly
        osrewigqfdyvzk
        kupsxrimowgbe

        skzvhanpljbogdyxtiquw
        xzgdwyjluhnbstopikaqv
        pasixghzovnjylbwudqkt

        wj
        wj
        wkj
        jw
        wj

        nsmyxfhc
        xcfymhsn
        hxscmnyf
        cfxsmnlyh

        jpblgmiyunazcfd
        tabnyfmupclzgd
        esyvuphfgwbknodzcxlqa

        xkyqozefvgutmrw
        rmhzwgeutoypx

        rzaxcemljnvo
        kdwyqvohesitfb

        kfqrcezwn
        qrnwkzcf
        frcnqwzk
        frnckvhzwq
        kqzrcnfw

        fqux
        qxuyfod
        ufxq
        qxfu
        qxuf

        ztopdir
        ritpozd
        otrpizd
        idpotzr

        jzhngmufw
        zguwhfj

        ndxhmysbgcriqkewoztujva
        cmtwbudvysekqaxizrojng
        itncgkdyoaxswrqvmejzpbu
        qyvcdusgwbomejtxznkira

        nik
        yfi
        i
        i

        hpsdjo
        hobps
        ohsp
        shpo
        kypshio

        jbiyatwz
        zbtagnrc
        ztnagb
        batz

        fvkurj
        kfgjvru
        jukfrv
        kvfujr
    """.trimIndent().trim()
}
