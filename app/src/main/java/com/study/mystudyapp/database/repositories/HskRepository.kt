package com.study.mystudyapp.database.repositories

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.study.mystudyapp.Coroutine
import com.study.mystudyapp.database.room.AppDatabase
import com.study.mystudyapp.database.room.words.WordsTable
import com.study.mystudyapp.observeOnce

class HskRepository(private val db: AppDatabase) {


    fun getWords(category: String) = db.getWordsTableDao().getAllWords()

    fun check(lifecycleOwner: LifecycleOwner) {
        db.getWordsTableDao().getAllWords()
            .observeOnce(lifecycleOwner, Observer {
                if (it == null || it.isEmpty()) {
                    insertWords()
                }
            })
    }


    private fun insertWords() {

        Coroutine.main {

            var w = ""
            val ww = mutableListOf<String>()
            val pp = mutableListOf<String>()
            val mm = mutableListOf<String>()

            words.forEach { c ->
                if (c == '\n') {
                    ww.add(w)
                    w = ""

                } else {
                    w += c
                }
            }

            pinyin.forEach { c ->
                if (c == '\n') {
                    pp.add(w)
                    w = ""

                } else {
                    w += c
                }
            }

            meaning.forEach { c ->
                if (c == '\n') {
                    mm.add(w)
                    w = ""
                } else {
                    w += c
                }

            }

            ww.forEachIndexed { index, s ->
                db.getWordsTableDao().insert(
                    WordsTable(
                        word = s,
                        pinyin = pp[index],
                        meaning = mm[index],
                        category = "1",
                        type = "word",
                    )
                )
            }

        }

        Coroutine.main {

            var w = ""
            val ww = mutableListOf<String>()
            val pp = mutableListOf<String>()
            val mm = mutableListOf<String>()

            sentWords.forEach { c ->
                if (c == '\n') {
                    ww.add(w)
                    w = ""

                } else {
                    w += c
                }
            }

            sentPinyin.forEach { c ->
                if (c == '\n') {
                    pp.add(w)
                    w = ""

                } else {
                    w += c
                }
            }

            sentMeaning.forEach { c ->
                if (c == '\n') {
                    mm.add(w)
                    w = ""
                } else {
                    w += c
                }

            }

            ww.forEachIndexed { index, s ->
                db.getWordsTableDao().insert(
                    WordsTable(
                        word = s,
                        pinyin = pp[index],
                        meaning = mm[index],
                        category = "1",
                        type = "sent",
                        )
                )
            }

        }
    }


    private val sentPinyin = "8 diǎn 40 fēn\n" +
            "2009 nián 7 yuè 6 rì\n" +
            "xīngqīsì\n" +
            "Tā jīnnián 24 suì．\n" +
            "15 kuài\n" +
            "Wǒ de diànhuà shì 58590000.\n" +
            "yí ge\n" +
            "3 běn\n" +
            "zhège\n" +
            "nàxiē\n" +
            "jǐ běn\n" +
            "Wǒ bú shì xuésheng．\n" +
            "Tā méi qù yīyuàn．\n" +
            "Tā hěn gāoxìng．\n" +
            "Tài hǎo le!\n" +
            "Wǒmen dōu kànjiàn nàge rén le．\n" +
            "wǒ hé nǐ\n" +
            "Wǒ zhù zài Běijīng．\n" +
            "Wǒ huì zuò fàn．\n" +
            "Nǐ shénme shíhou néng lái?\n" +
            "wǒ de diànnǎo\n" +
            "Tā qù yīyuàn le．\n" +
            "Tā shì yīshēng ma?\n" +
            "Nǐ zài nǎr ne?\n" +
            "Wéi, nǐ hǎo.\n" +
            "Míngtiān xīngqīliù．\n" +
            "Wǒ rènshi tā．\n" +
            "Tiānqì hěn hǎo．\n" +
            "Tā bú zài fàndiàn．\n" +
            "Tā méi qù kàn diànyǐng．\n" +
            "Zhè shì nǐ de zhuōzi ma?\n" +
            "Wǒ shì lǎoshī, nǐ ne?\n" +
            "Nàge rén shì shéi?\n" +
            "Zhèxiē bēizi， nǐ xǐhuan nǎ yí ge?\n" +
            "Nǐ xiǎng qù nǎr?\n" +
            "Nǐ ài chī shénme shuǐguǒ?\n" +
            "Nǐmen xuéxiào yǒu duōshao xuésheng?\n" +
            "Nǐ jǐ suì le?\n" +
            "Nǐ zěnme le?\n" +
            "Zhè běn shū zěnmeyàng?\n" +
            "Qǐng zuò．\n" +
            "Tài hǎo le!\n" +
            "Tā shì wǒ de tóngxué．\n" +
            "Yìnián yǒu 12 ge yuè．\n" +
            "Wǒ shì zuótiān lái de．\n" +
            "Zhè shì zài huǒchēzhàn mǎi de．\n" +
            "Tā shì zuò fēijī lái de．\n" +
            "Tāmen zài chī fàn ne．\n"

    private val sentMeaning = "8:40 (time of day)\n" +
            "6th July 2009\n" +
            "Thursday\n" +
            "He is 24 this year.\n" +
            "RMB15\n" +
            "My phone number is 58590000.\n" +
            "One item (m.w., general)\n" +
            "Three items (m.w., e.g. books)\n" +
            "This\n" +
            "Those\n" +
            "Several/How many items (e.g. books)\n" +
            "I am not a student.\n" +
            "He didn't go to hospital.\n" +
            "She is very happy.\n" +
            "That's great!\n" +
            "We all saw that man.\n" +
            "Me and you\n" +
            "I live in Beijing.\n" +
            "I can cook.\n" +
            "What time can you come?\n" +
            "My computer\n" +
            "She went to hospital.\n" +
            "Is he a doctor?\n" +
            "Where are you?\n" +
            "Hello? (on the phone)\n" +
            "Tomorrow is Saturday.\n" +
            "I know him.\n" +
            "The weather is good.\n" +
            "She is not at the hotel/restaurant.\n" +
            "She didn't go to the movie.\n" +
            "Is this your table?\n" +
            "I am a teacher. What about you?\n" +
            "Who is that person?\n" +
            "Which one of these cups do you like?\n" +
            "Where do you want to go?\n" +
            "Which fruit do you like?\n" +
            "Your school has how many students?\n" +
            "How old are you?\n" +
            "Are you okay/What happened to you?\n" +
            "How is this book?\n" +
            "Have a seat.\n" +
            "That's great!\n" +
            "He is my classmate.\n" +
            "A year has 12 months.\n" +
            "I arrived yesterday.\n" +
            "I bought this at the train station. \n" +
            "He came here by plane.\n" +
            "They are eating.\n"


    private val sentWords = "8点40分\n" +
            "2009年7月6日\n" +
            "星期四\n" +
            "他今年24岁。\n" +
            "15块\n" +
            "我的电话是58590000。\n" +
            "一个\n" +
            "3本\n" +
            "这个\n" +
            "那些\n" +
            "几本\n" +
            "我不是学生。\n" +
            "他没去医院。\n" +
            "她很高兴。\n" +
            "太好了！\n" +
            "我们都看见那个人了。\n" +
            "我和你\n" +
            "我住在北京。\n" +
            "我会做饭。\n" +
            "你什么时候能来？\n" +
            "我的电脑\n" +
            "她去医院了。\n" +
            "他是医生吗？\n" +
            "你在哪儿呢？\n" +
            "喂，你好。\n" +
            "明天星期六。\n" +
            "我认识他。\n" +
            "天气很好。\n" +
            "她不在饭店。\n" +
            "她没去看电影。\n" +
            "这是你的桌子吗？\n" +
            "我是老师，你呢？\n" +
            "那个人是谁？\n" +
            "这些杯子，你喜欢哪一个？\n" +
            "你想去哪儿？\n" +
            "你爱吃什么水果？\n" +
            "你们学校有多少学生？\n" +
            "你几岁了？\n" +
            "你怎么了？\n" +
            "这本书怎么样？\n" +
            "请坐。\n" +
            "太好了！\n" +
            "他是我的同学。\n" +
            "一年有12个月。\n" +
            "我是昨天来的。\n" +
            "这是在火车站买的。\n" +
            "他是坐飞机来的。\n" +
            "他们在吃饭呢。\n"

    private val pinyin = "ài\n" +
            "bā\n" +
            "bàba\n" +
            "bēizi\n" +
            "Běijīng\n" +
            "běn\n" +
            "bú kèqi\n" +
            "bù\n" +
            "cài\n" +
            "chá\n" +
            "chī\n" +
            "chūzūchē\n" +
            "dǎ diànhuà\n" +
            "dà\n" +
            "de\n" +
            "diǎn\n" +
            "diànnǎo\n" +
            "diànshì\n" +
            "diànyǐng\n" +
            "dōngxi\n" +
            "dōu\n" +
            "dú\n" +
            "duìbuqǐ\n" +
            "duō\n" +
            "duōshao\n" +
            "érzi\n" +
            "èr\n" +
            "fàndiàn\n" +
            "fēijī\n" +
            "fēnzhōng\n" +
            "gāoxìng\n" +
            "ge\n" +
            "gōngzuò\n" +
            "gǒu\n" +
            "Hànyǔ\n" +
            "hǎo\n" +
            "hào\n" +
            "hē\n" +
            "hé\n" +
            "hěn\n" +
            "hòumian\n" +
            "huí\n" +
            "huì\n" +
            "jǐ\n" +
            "jiā\n" +
            "jiào\n" +
            "jīntiān\n" +
            "jiǔ\n" +
            "kāi\n" +
            "kàn\n" +
            "kànjiàn\n" +
            "kuài\n" +
            "lái\n" +
            "lǎoshī\n" +
            "le\n" +
            "lěng\n" +
            "lǐ\n" +
            "liù\n" +
            "māma\n" +
            "ma\n" +
            "mǎi\n" +
            "māo\n" +
            "méi guānxi\n" +
            "méiyǒu\n" +
            "mǐfàn\n" +
            "míngtiān\n" +
            "míngzi\n" +
            "nǎa\n" +
            "nǎr\n" +
            "nà\n" +
            "ne\n" +
            "néng\n" +
            "nǐ\n" +
            "nián\n" +
            "nǚ'ér\n" +
            "péngyou\n" +
            "piàoliang\n" +
            "píngguǒ\n" +
            "qī\n" +
            "qián\n" +
            "qiánmiàn\n" +
            "qǐng\n" +
            "qù\n" +
            "rè\n" +
            "rén\n" +
            "rènshi\n" +
            "sān\n" +
            "shāngdiàn\n" +
            "shàng\n" +
            "shàngwǔ\n" +
            "shǎo\n" +
            "shéi\n" +
            "shénme\n" +
            "shí\n" +
            "shíhou\n" +
            "shì\n" +
            "shū\n" +
            "shuǐ\n" +
            "shuǐguǒ\n" +
            "shuì jiào\n" +
            "shuō\n" +
            "sì\n" +
            "suì\n" +
            "tā\n" +
            "tā\n" +
            "tài\n" +
            "tiānqì\n" +
            "tīng\n" +
            "tóngxué\n" +
            "wèi\n" +
            "wǒ\n" +
            "wǒmen\n" +
            "wǔ\n" +
            "xǐhuan\n" +
            "xià\n" +
            "xiàwǔ\n" +
            "xiàyǔ\n" +
            "xiānsheng\n" +
            "xiànzài\n" +
            "xiǎng\n" +
            "xiǎo\n" +
            "xiǎojie\n" +
            "xiē\n" +
            "xiě\n" +
            "xièxie\n" +
            "xīngqī\n" +
            "xuésheng\n" +
            "xuéxí\n" +
            "xuéxiào\n" +
            "yī\n" +
            "yīfu\n" +
            "yīshēng\n" +
            "yīyuàn\n" +
            "yǐzi\n" +
            "yìdiǎnr\n" +
            "yǒu\n" +
            "yuè\n" +
            "zài\n" +
            "zàijiàn\n" +
            "zěnme\n" +
            "zěnmeyàng\n" +
            "zhè\n" +
            "Zhōngguó\n" +
            "zhōngwǔ\n" +
            "zhù\n" +
            "zhuōzi\n" +
            "zì\n" +
            "zuótiān\n" +
            "zuò\n" +
            "zuò\n"


    private val meaning = "love\n" +
            "eight\n" +
            "Dad\n" +
            "cup; glass\n" +
            "Beijing\n" +
            "measure word for books\n" +
            "you're welcome; don't be polite\n" +
            "no; not\n" +
            "dish (type of food); vegetables\n" +
            "tea\n" +
            "eat\n" +
            "taxi; cab\n" +
            "make a phone call\n" +
            "big; large\n" +
            "indicates possession, like adding 's to a noun\n" +
            "a dot; a little; o'clock\n" +
            "computer\n" +
            "television; TV\n" +
            "movie; film\n" +
            "things; stuff\n" +
            "all; both\n" +
            "to read; to study\n" +
            "sorry\n" +
            "many\n" +
            "how much?; how many?\n" +
            "son\n" +
            "two\n" +
            "restaurant; hotel\n" +
            "airplane\n" +
            "minute; (measure word for time)\n" +
            "happy; glad\n" +
            "general measure word\n" +
            "work; a job\n" +
            "dog\n" +
            "Chinese language\n" +
            "good\n" +
            "number; day of a month\n" +
            "to drink\n" +
            "and; with\n" +
            "very; quite\n" +
            "back; behind\n" +
            "to return; to reply; to go back\n" +
            "know how to\n" +
            "how many; several; a few\n" +
            "family; home\n" +
            "to be called\n" +
            "today\n" +
            "nine\n" +
            "to open; to start; to operate (a vehicle)\n" +
            "see; look at; to watch\n" +
            "see; catch sight of\n" +
            "lump; piece; sum of money\n" +
            "come; arrive; ever since; next\n" +
            "teacher\n" +
            "indicates a completed or finished action\n" +
            "cold\n" +
            "inside; Chinese mile (~.5 km)\n" +
            "six\n" +
            "mom; mum\n" +
            "indicates a yes/no question (added to a statement)\n" +
            "to buy\n" +
            "cat\n" +
            "it doesn't matter; never mind\n" +
            "not have; there is not\n" +
            "(cooked) rice\n" +
            "tomorrow\n" +
            "name\n" +
            "which; how\n" +
            "where? (Beijing accent)\n" +
            "that; then\n" +
            "indicates a question; how about...?;\n" +
            "can; be able\n" +
            "you (singular)\n" +
            "year\n" +
            "daughter\n" +
            "friend\n" +
            "pretty; beautiful\n" +
            "apple\n" +
            "seven\n" +
            "money; coin\n" +
            "in front\n" +
            "please; invite; to treat someone to something\n" +
            "go; to leave\n" +
            "heat; hot\n" +
            "person; man; people\n" +
            "recognize; know (a person)\n" +
            "three\n" +
            "shop; store\n" +
            "above; up\n" +
            "late morning (before noon)\n" +
            "few; little\n" +
            "who\n" +
            "what? (replaces the noun to turn a statement into a question)\n" +
            "ten\n" +
            "time\n" +
            "be; is; are; am\n" +
            "book; letter\n" +
            "water\n" +
            "fruit\n" +
            "to sleep; go to bed\n" +
            "speak\n" +
            "four\n" +
            "years old; age\n" +
            "he; him\n" +
            "she\n" +
            "too (much)\n" +
            "weather\n" +
            "listen; hear\n" +
            "fellow student; schoolmate\n" +
            "hello (on the phone)\n" +
            "I; me\n" +
            "we; us\n" +
            "five\n" +
            "to like\n" +
            "fall; below\n" +
            "afternoon\n" +
            "to rain\n" +
            "Mr.; Sir\n" +
            "now\n" +
            "think; believe; suppose; would like to\n" +
            "small; young\n" +
            "young lady; miss; Ms.\n" +
            "some; few; several\n" +
            "to write; to compose\n" +
            "thank you\n" +
            "week\n" +
            "student\n" +
            "learn; to study\n" +
            "school\n" +
            "one; once; a\n" +
            "clothes\n" +
            "doctor\n" +
            "hospital\n" +
            "chair\n" +
            "a bit; a few\n" +
            "have\n" +
            "moon; month\n" +
            "at; on; in; indicates an action in progress\n" +
            "goodbye; see you later\n" +
            "how?\n" +
            "how about?; how is/was it?\n" +
            "this\n" +
            "China\n" +
            "noon; midday\n" +
            "to live; reside; to stop\n" +
            "table; desk\n" +
            "letter; character\n" +
            "yesterday\n" +
            "do; make\n" +
            "sit\n"

    private val words = "爱\n" +
            "八\n" +
            "爸爸\n" +
            "杯子\n" +
            "北京\n" +
            "本\n" +
            "不客气\n" +
            "不\n" +
            "菜\n" +
            "茶\n" +
            "吃\n" +
            "出租车\n" +
            "打电话\n" +
            "大\n" +
            "的\n" +
            "点\n" +
            "电脑\n" +
            "电视\n" +
            "电影\n" +
            "东西\n" +
            "都\n" +
            "读\n" +
            "对不起\n" +
            "多\n" +
            "多少\n" +
            "儿子\n" +
            "二\n" +
            "饭店\n" +
            "飞机\n" +
            "分钟\n" +
            "高兴\n" +
            "个\n" +
            "工作\n" +
            "狗\n" +
            "汉语\n" +
            "好\n" +
            "号\n" +
            "喝\n" +
            "和\n" +
            "很\n" +
            "后面\n" +
            "回\n" +
            "会\n" +
            "几\n" +
            "家\n" +
            "叫\n" +
            "今天\n" +
            "九\n" +
            "开\n" +
            "看\n" +
            "看见\n" +
            "块\n" +
            "来\n" +
            "老师\n" +
            "了\n" +
            "冷\n" +
            "里\n" +
            "六\n" +
            "妈妈\n" +
            "吗\n" +
            "买\n" +
            "猫\n" +
            "没关系\n" +
            "没有\n" +
            "米饭\n" +
            "明天\n" +
            "名字\n" +
            "哪\n" +
            "哪儿\n" +
            "那\n" +
            "呢\n" +
            "能\n" +
            "你\n" +
            "年\n" +
            "女儿\n" +
            "朋友\n" +
            "漂亮\n" +
            "苹果\n" +
            "七\n" +
            "钱\n" +
            "前面\n" +
            "请\n" +
            "去\n" +
            "热\n" +
            "人\n" +
            "认识\n" +
            "三\n" +
            "商店\n" +
            "上\n" +
            "上午\n" +
            "少\n" +
            "谁\n" +
            "什么\n" +
            "十\n" +
            "时候\n" +
            "是\n" +
            "书\n" +
            "水\n" +
            "水果\n" +
            "睡觉\n" +
            "说\n" +
            "四\n" +
            "岁\n" +
            "他\n" +
            "她\n" +
            "太\n" +
            "天气\n" +
            "听\n" +
            "同学\n" +
            "喂\n" +
            "我\n" +
            "我们\n" +
            "五\n" +
            "喜欢\n" +
            "下\n" +
            "下午\n" +
            "下雨\n" +
            "先生\n" +
            "现在\n" +
            "想\n" +
            "小\n" +
            "小姐\n" +
            "些\n" +
            "写\n" +
            "谢谢\n" +
            "星期\n" +
            "学生\n" +
            "学习\n" +
            "学校\n" +
            "一\n" +
            "衣服\n" +
            "医生\n" +
            "医院\n" +
            "椅子\n" +
            "一点儿\n" +
            "有\n" +
            "月\n" +
            "在\n" +
            "再见\n" +
            "怎么\n" +
            "怎么样\n" +
            "这\n" +
            "中国\n" +
            "中午\n" +
            "住\n" +
            "桌子\n" +
            "字\n" +
            "昨天\n" +
            "做\n" +
            "坐\n"

}