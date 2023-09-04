import spock.lang.Specification

class templateExampleSpockTestCase extends Specification {



/*
    // Renewal of U+ Authentication process automation
    def "[서비스공통-#NO] U+ Auth 인증 처리/갱신 - #DESC"() {
        expect:
        def results = authenticateAndCommunicate.getBuildForAuth().doRequestWithGenForAuth()

        results.checkResultsForAuth(RET, results.getResStatus(), EXEPT_VAL, results.getValueFromMap("authErrMsg"), "VALID_ACCESS_TOKEN").isEmpty()

        where:
        NO      | RET  |   EXEPT_VAL   |   DESC
        "001"   |  200 |   401         |   "인증토큰-갱신-정상-케이스"
    }

    // Example nerget service testing api, subscribe social
    def "[#SERVICE_NAME-#NO] 소셜가입 - #DESC"() {
        expect:
        def results = authenticateAndCommunicate.getBuild(PATH).doRequestWithGen(METHOD, BODY, null, null, SERVICE_NAME)

        results.checkResultsForRequest(NO, RET, results.getResStatus(), EXEPT_VAL, results.getValueFromMap("responseMsg"), "B-002").isEmpty()

        where:
        NO    | SERVICE_NAME    |RET | EXEPT_VAL | METHOD | PATH            | BODY                     | DESC
        "001" | "nerget"        |200 | 400       | "POST" | "api/v1/social" | "{\"termIds\": [1, 2] }" | "정상-케이스 : 기존 가입자"
    }
*/

    //  답다 API

    def "[#SERVICE_NAME-#NO] 일기생성 - #DESC"() {
        expect:
        def result = authenticateAndCommunicate.getBuild(PATH).doRequestWithGen(METHOD, BODY, null, null, SERVICE_NAME)

        result.checkResultsForRequest(NO, RET, result.getResStatus(), EXEPT_VAL, result.getValueFromMap("message"), "Diary already exists on same date" ).isEmpty()


        where:
        NO    | SERVICE_NAME    |RET | EXEPT_VAL | METHOD | PATH                  | BODY                     | DESC
        "001" | "Dapda "        |201 | 400       | "POST" | "api/v1/diary/posts" | "{\n" +
                "  \"diaryDate\": \"2022-10-05\",\n" +
                "  \"content\": \"오늘 날씨 좋다\",\n" +
                "  \"emotionCategory\": [\n" +
                "    \"기록용 일기4444\"\n" +
                "  ],\n" +
                "  \"emotionType\": [\n" +
                "    \"날씨기록용 일기날씨기록용 일기날씨기록용 일기날씨기록용 일기날씨기록용 일기  기록용 일기\"\n" +
                "  ]\n" +
                "}" | "정상-케이스"

    }

    def "[#SERVICE_NAME-#NO] 일기조회 - #DESC"() {
        expect:
        def result = authenticateAndCommunicate.getBuild(PATH, V_ONE, PATH_TWO, V_TWO).doRequestWithGen(METHOD, null, null, null, SERVICE_NAME)

        result.checkResultsForRequest(NO, RET, result.getResStatus(), EXEPT_VAL, result.getValueFromMap("responseMsg"), "B-002" ).isEmpty()


        where:
        NO    | SERVICE_NAME    |RET | EXEPT_VAL | METHOD | PATH                               |   V_ONE           | PATH_TWO                        |   V_TWO             | DESC
        "001" | "Dapda "        |200 | 401       | "GET" | "api/v1/diary/posts?diaryDateGte="  |   "2022-11-11"    |   "&diaryDateLte="              | "2023-08-08"        |"정상-케이스"
        "002" | "Dapda "        |400 | 400       | "GET" | "api/v1/diary/posts?diaryDateGte="  |   ""              |   "&diaryDateLte="              | "2023-08-08"        |"Parameter/body값 없음"
        "003" | "Dapda "        |400 | 400       | "GET" | "api/v1/diary/posts?diaryDateGte="  |   "2022-11-11"    |   "&diaryDateLte="              | ""                  |"Parameter/body값 없음"
        "004" | "Dapda "        |400 | 400       | "GET" | "api/v1/diary/posts?diaryDateGte="  |   "2022-11-33"    |   "&diaryDateLte="              | "2023-08-08"        |"Size 늘림"
        "005" | "Dapda "        |400 | 400       | "GET" | "api/v1/diary/posts?diaryDateGte="  |   "2022-11-11"    |   "&diaryDateLte="              | "2023-08-40"        |"Size 줄임"
        "006" | "Dapda "        |400 | 400       | "GET" | "api/v1/diary/posts?diaryDateGte="  |   "2022-11-!#"    |   "&diaryDateLte="              | "2023-08-08"        |"특수 문자"
        "007" | "Dapda "        |400 | 400       | "GET" | "api/v1/diary/posts?diaryDateGte="  |   "2022-11 11"    |   "&diaryDateLte="              | "2023-08-08"        |"띄어쓰기"
        "008" | "Dapda "        |400 | 400       | "GET" | "api/v1/diary/posts?diaryDateGte="  |   ""              |   "&diaryDateLte="              | ""                  |"Parameter/body없음"
    }

    def "[#SERVICE_NAME-#NO] 일기삭제 - #DESC"() {
        expect:
        def result = authenticateAndCommunicate.getBuild(PATH, V_ONE).doRequestWithGen(METHOD, null, null, null, SERVICE_NAME)

        result.checkResultsForRequest(NO, RET, result.getResStatus(), EXEPT_VAL, result.getValueFromMap("responseMsg"), "B-002" ).isEmpty()


        where:
        NO    | SERVICE_NAME    |RET | EXEPT_VAL | METHOD   | PATH                  |   V_ONE         | DESC
        "001" | "Dapda "        |200 | 401       | "DELETE" | "api/v1/diary/posts/"  |   str_did    | "정상-케이스"

    }

}