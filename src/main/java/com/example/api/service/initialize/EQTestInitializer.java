package com.example.api.service.initialize;

import com.example.api.model.test.*;
import com.example.api.service.TestManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class EQTestInitializer implements CommandLineRunner {

    private static final String NAME = "EQ";

    private static final String DESCRIPTION = "Тест на эмоциональный коэффициент (EQ)";

    private static final List<Integer> First = Arrays.asList(1, 2, 3, 4);

    private static final List<Integer> Second = Arrays.asList(4, 3, 2, 1);

    @Autowired
    private TestManagementService testManagementService;

    @Override
    public void run(String... args) {
        initializeTestData();
    }

    private void initializeTestData() {
        log.info("Initializing test data...");
        if (testManagementService.existTestByName(NAME)) {
            log.error("Test already exists!");
            return;
        }

        log.info("Creating test data...");
        Test newTest = new Test();
        newTest.setName(NAME);
        newTest.setDescription(DESCRIPTION);
        testManagementService.saveTest(newTest);

        createQuestion(newTest);
        createResult(newTest);

        log.info("Initializing test data successful");
    }


    private void createQuestion(Test newTest) {
        log.info("Creating questions : {}", newTest.getName());

        createQuestion(newTest, 1, First, "Я часто хандрю и пребываю в мрачном настроении без видимой причины");
        createQuestion(newTest, 2, Second, "Я умею «отпускать вожжи» и не пытаюсь всегда и все контролировать в моей жизни");
        createQuestion(newTest, 3, Second, "В кино я благодарный зритель – меня легко рассмешить и заставить плакать, и мне это нравится");
        createQuestion(newTest, 4, First, "В повседневной жизни эмоции часто мне мешают");
        createQuestion(newTest, 5, First, "Когда я в сильном гневе, я могу что-нибудь сломать, разбить и даже ударить человека");

        createQuestion(newTest, 6, Second, "Я легко вижу, когда мне говорят неправду");
        createQuestion(newTest, 7, Second, "Я слежу за тем, чтобы получать удовольствие от маленьких радостей жизни");
        createQuestion(newTest, 8, First, "Я могу заболеть от собственных эмоций");
        createQuestion(newTest, 9, First, "У меня есть стойкие симпатии и антипатии, и я с трудом меняю свое мнение о людях");
        createQuestion(newTest, 10, Second, "Если что-то вывело меня из себя, мне удается довольно быстро успокоиться");

        createQuestion(newTest, 11, Second, "Когда мне нужно выступать перед аудиторией, я почти не ощущаю физических проявлений волнения");
        createQuestion(newTest, 12, First, "Я страдаю «синдромом скороварки»: если что-то меня раздражает, я долго сдерживаюсь, а потом взрываюсь");
        createQuestion(newTest, 13, First, "Мне часто случается дуться, обижаться на кого-нибудь");
        createQuestion(newTest, 14, Second, "Если у меня возникает проблема в отношениях с кем-то, я легко могу поговорить с ним об этом");
        createQuestion(newTest, 15, First, "В детстве я часто капризничал(а)");

        createQuestion(newTest, 16, Second, "Я умею нравиться и добиваться популярности");
        createQuestion(newTest, 17, Second, "Я обладаю хорошей интуицией в человеческих отношениях, хорошо чувствую людей");
        createQuestion(newTest, 18, First, "Мне легко внушить чувство вины");
        createQuestion(newTest, 19, Second, "Люди охотно изливают мне душу");
        createQuestion(newTest, 20, First, "Мне часто случается действовать в ущерб своим интересам под влиянием эмоций");

        createQuestion(newTest, 21, First, "Когда у меня плохое настроение, я посылаю всех куда подальше");
        createQuestion(newTest, 22, First, "Мне очень трудно принимать критику, даже обоснованную и конструктивную");
        createQuestion(newTest, 23, Second, "Я хороший дипломат");
        createQuestion(newTest, 24, Second, "Чтобы хорошо себя чувствовать, мне нужно выражать себя в искусстве (в живописи, музыке...)");
        createQuestion(newTest, 25, First, "Когда я слишком взволнован (а) хорошей новостью, я могу потерять сон");

        createQuestion(newTest, 26, First, "Я слишком много работаю - вплоть до того, что жертвую своим отдыхом и развлечениями");
        createQuestion(newTest, 27, Second, "Я довольно часто медитирую или молюсь");
        createQuestion(newTest, 28, First, "Мне всегда было трудно сказать «я тебя люблю»");
        createQuestion(newTest, 29, Second, "Я нуждаюсь в регулярном общении с природой");
        createQuestion(newTest, 30, First, "В детстве родители недостаточно прислушивались к моим эмоциям");

        createQuestion(newTest, 31, First, "Я могу долго держать зло на тех, кто причинил мне боль");
        createQuestion(newTest, 32, Second, "Я внимательно отношусь к качеству своей жизни");
        createQuestion(newTest, 33, Second, "Я мирюсь с тем, что не могу все узнать, все понять или все объяснить в своей жизни и в жизни других людей");
        createQuestion(newTest, 34, Second, "Когда мне грустно, я стараюсь поднять себе настроение и не позволить себе впасть в уныние");
        createQuestion(newTest, 35, Second, "Когда я что-то предлагаю коллегам (или выступаю на собрании), ко мне часто прислушиваются");

        createQuestion(newTest, 36, Second, "Я испытываю неловкость, когда вижу наигранные эмоции, вроде тех, что присутствуют по сценарию в некоторых телепередачах");
        createQuestion(newTest, 37, Second, "Я предпочитаю читать романы, а не очерки и эссе");
        createQuestion(newTest, 38, Second, "Когда я мысленно возвращаюсь к своим самым светлым воспоминаниям, я вновь испытываю сильные эмоции");
        createQuestion(newTest, 39, First, "Мне часто требуется время, чтобы осознать, что я нервничаю");
        createQuestion(newTest, 40, Second, "Я в состоянии выслушать и понять точку зрения собеседника, даже если я ее не разделяю");

        createQuestion(newTest, 41, First, "Иногда я плохо понимаю свои эмоциональные реакции");
        createQuestion(newTest, 42, Second, "Я в значительной степени доверяю своей интуиции при принятии решений");
    }

    private void createResult(Test newTest) {
        log.info("Creating possible results : {}", newTest.getName());


        TestResult generalEQ = createResult(newTest, EQResults.GENERAL_EQ);
        TestResult selfAwareness = createResult(newTest, EQResults.SELF_AWARENESS);
        TestResult socialInteraction = createResult(newTest, EQResults.SOCIAL_INTERACTION);
        TestResult lifeAttitude = createResult(newTest, EQResults.LIFE_ATTITUDE);

        createPossibleResult(generalEQ, 121, 168, """
                У вас высокий уровень эмоционального интеллекта. Это преимущество позволяет вам приспосабливаться к\
                 любой ситуации. Вы по-настоящему понимаете себя на телесном и интуитивном уровне, а это нечто гораздо\
                 большее, чем «знание себя», основанное на самонаблюдении и рефлексии. Ваше «Я» — ваш партнер. Вы хорошо\
                 владеете собой и большую часть времени находитесь в ровном и позитивном расположении духа. Ваши\
                 отношения с другими людьми содержательны, вы используете свои эмоции и интуицию для того, чтобы понять\
                 их и окружающий вас мир.""");

        createPossibleResult(generalEQ, 81, 120, """
                Ваш уровень эмоционального интеллекта таков, как и у большинства окружающих вас людей. Вы находитесь\
                 посередине, и это совсем неплохо! Вы хорошо понимаете других и успешно управляете своими эмоциями, но\
                 можете делать это еще лучше. Работайте над проявлениями своих эмоций, обращайте внимание на их\
                 физические симптомы и на свое настроение, ищите их причину. Ухаживайте за собой (тело — это орудие\
                 эмоционального интеллекта), развивайте свои творческие способности и духовное начало (наши эмоции\
                 возникают из невербального опыта); волушивайтесь в других и старайтесь их понять (их эмоции позволяют\
                 нам многое узнать о себе). Наконец, будьте толерантны к себе, поскольку постоянная война с собой\
                 порождает множество негативных эмоций.""");

        createPossibleResult(generalEQ, 42, 80, """              
                У вас невысокий уровень эмоционального интеллекта. Это не значит, что у вас отсутствуют эмоции, просто\
                 вы пользуетесь ими очень ограниченно. Вы оставляете в стороне существенную часть себя, непознанную и\
                 порой доставляющую неудобства, которую вы плохо понимаете и которая порой раздражает вас. В результате\
                 возникает напряженность в ваших отношениях с собой и, следовательно, в ваших отношениях с другими. Это\
                 говорит скорее о недооценке эмоциональной сферы, чем об отсутствии соответствующих способностей. Вам\
                 даны эмоции, остается лишь научиться понимать их и использовать. Обратитесь к книгам, обсудите\
                 прочитанное с близкими. А еще начните постепенно вносить небольшие перемены в вашу повседневную жизнь.\
                 Вы увидите: она вполне может стать лучше благодаря эмоциям!""");


        createPossibleResult(selfAwareness, 36, 56, """
                Ваши эмоции редко перехлестывают через край вы умеете сделать их своими незаменимыми помощниками в\
                 повседневной жизни, Вы хорошо себя знаете и способны распознать негативные чувства (гнев, грусть,\
                 беспокойство, зависть), когда они только возникают, установить их причины и принять соответствующие\
                 меры. Вы, безусловно, не идете на поводу у своих эмоций; они оживляют и вдохновляют вас, что гораздо\
                 удобнее и полезнее.""");

        createPossibleResult(selfAwareness, 14, 35, """
                Ваши эмоции приносят вам одни неудобства. Часто вы либо подавляете их, либо срываетесь. Вы плохо\
                 понимаете собственные чувства и не прислушиваетесь к ним либо обращаете на них внимание, когда уже\
                 слишком поздно и они вышли из-под контроля. Вы позволяете себе подолгу жить под воздействием\
                 отрицательных переживаний, которые иногда заставляют вас действовать себе во вред (дуться, ссориться,\
                 замыкаться в себе). Постарайтесь уделять больше внимания своим эмоциям и делать это вовремя.\
                 Воспринимайте их как сигнал тревоги: они указывают вам, что нужно что-то делать.""");


        createPossibleResult(socialInteraction, 36, 56, """
                Вы способны продуктивно использовать ваши эмоции в отношениях с другими людьми. Прежде всего — благодаря\
                 умению слушать, основанному на интуиции (вы чувствуете, что стоит за словами и намерениями) и эмпатии\
                 (вы чувствуете и понимаете змоциональное состояние собеседника). Затем — в силу вашего мастерства в\
                 области построения отношений: вы умеете выражать то, что хотите сказать, не задевая ничьих чувств, и в\
                 то же время ваши высказывания всегда учитывают индивидуальность собеседника, что делает их\
                 убедительными. Как в случае эмоционально позитивного (нежность, комплименть...), так и в случае\
                 эмоционально, негативного (критика, разногласия...) общения вы умеете отстаивать свою позицию и\
                 доносить ее до собеседников. В то же время вы заранее учитываете возможность того, что с вами не\
                 согласятся, и это не может вывести вас из эмоционального равновесия.""");

        createPossibleResult(socialInteraction, 14, 35, """
                В ваших отношениях с другими вас бросает из крайности в крайность: вы либо слишком колючи и агрессивны,\
                 либо чрезмерно податливы и покорны. Отсюда чередование силовых приемов, раздражающих ваших собеседников,\
                 и уступок, вызывающих фрустрацию у вас самих. Ваши эмоции часто мешают вам слушать других и выражать\
                 себя. Из-за этого вы можете производить очень противоречивое впечатление человека непредсказуемого,\
                 подверженного чужому влиянию, нецелеустремленного, холодного, отстраненного...
                Вместо того чтобы подавлять свои эмоции, лучше сделайте их частью ваших посланий, адресованных другим.\
                 Включайте в свои высказывания как положительные, так и отрицательные эмоции: чаще говорите «мне\
                 приятно», «я рад(а)», «это меня смущает, беспокоит». Всегда лучше выбирать объяснения и диалог, чем\
                 злиться и в который раз перебирать в уме обиды.""");


        createPossibleResult(lifeAttitude, 36, 56, """
                Вы ясно понимаете важность положительного самоощущения и личностного развития как жизненных приоритетов.\
                 А раз так, то вы знаете, что, жертвуя собой, надрываясь на работе или проявляя излишнюю инициативу, не\
                 стоит заходить слишком далеко (или слишком надолго). Вы прилагаете усилия к тому, чтобы развивать свои\
                 творческие способности, открытость, и умеете «отпустить вожжи», расслабиться. Благодаря этому ваша\
                 предрасположенность к удовлетворенностью жизнью оказывается высокой.""");

        createPossibleResult(lifeAttitude, 14, 35, """
                Не отдавая себе отчета, вы склонны жертвовать собственным самоощущением в пользу всего остального:\
                 семьи, работы, обязательств... Речь идет о настоящем самоотречении, связанном с неумением воспринимать\
                 и уважать те свои потребности, которые определенно не являются ни материальными, ни рациональными. Это\
                 приводит к некоторой уязвимости перед стрессом, вызывает срывы, экзистенциальные терзания и приступы\
                 усталости. Что если вам немного больше уделять внимания себе? Вам стоит учитывать ваши эмоциональные\
                 потребности при принятии решений: не подвергайте себя лишним стрессам, не отдаляйтесь от ваших\
                 устремлений и надежд. Меньше знаний и больше сознания, меньше контроля и больше непосредственности,\
                 меньше планирования и больше импровизации... Вложите чуть больше сил и энергии в качество жизни, и вы\
                 увидите, что в долгосрочной перспективе это ничуть не повредит вашим успехам. И даже наоборот.""");
    }

    private void createQuestion(Test test, Integer questionNumber, List<Integer> values, String content) {
        TestQuestion newQuestion = new TestQuestion();
        newQuestion.setTest(test);
        newQuestion.setNumber(questionNumber);
        newQuestion.setContent(content);
        testManagementService.saveQuestion(newQuestion);

        createPossibleAnswer(newQuestion, 1, values.get(0), EQAnswer.CORRECT.name());
        createPossibleAnswer(newQuestion, 2, values.get(1), EQAnswer.MOSTLY_CORRECT.name());
        createPossibleAnswer(newQuestion, 3, values.get(2), EQAnswer.MOSTLY_INCORRECT.name());
        createPossibleAnswer(newQuestion, 4, values.get(3), EQAnswer.INCORRECT.name());
    }

    private void createPossibleAnswer(TestQuestion question, Integer answerNumber, Integer value, String content) {
        PossibleAnswer newPossibleAnswer = new PossibleAnswer();
        newPossibleAnswer.setQuestion(question);
        newPossibleAnswer.setNumber(answerNumber);
        newPossibleAnswer.setValue(value);
        newPossibleAnswer.setContent(content);
        testManagementService.savePossibleAnswer(newPossibleAnswer);
    }

    private TestResult createResult(Test test, EQResults eqResults) {
        TestResult newResult = new TestResult();
        newResult.setTest(test);
        newResult.setName(eqResults.getName());
        newResult.setDescription(eqResults.getDescription());
        testManagementService.saveResult(newResult);
        return newResult;
    }

    private void createPossibleResult(TestResult newTestResult, Integer startValue, Integer endValue, String content) {
        PossibleResult newPossibleResults = new PossibleResult();
        newPossibleResults.setResult(newTestResult);
        newPossibleResults.setStartValue(startValue);
        newPossibleResults.setEndValue(endValue);
        newPossibleResults.setContent(content);
        testManagementService.savePossibleResult(newPossibleResults);
    }
}
