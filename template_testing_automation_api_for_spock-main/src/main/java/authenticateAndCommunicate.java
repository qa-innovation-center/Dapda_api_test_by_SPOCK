import com.google.gson.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Properties;

public class authenticateAndCommunicate {

    // Start : For managing object-json definition
    private JsonObject jsonObj;
    private HashMap<String, JsonObject> r;
    // Finish : For managing object-json definition

    // Start : For managing https keywords
    public static final String resValue = "Response";
    public static final String methodPut = "PUT";
    public static final String methodPost = "POST";
    public static final Integer LIMIT_RES_STATUS_VAL = 500;
    public static final String aVal = "acceptVal";
    // Finish : For managing https keywords

    // Start : Definition about environment path
    public static final String filePath = "src/env.txt";
    // Finish : Definition about environment path


    // Start : Specific definition keywords or environment value of authentication (U+Auth)
    public static final String aToken= "accessToken";
    public static final String rToken = "refreshToken";
    public static final String aDomain = "authDomain";
    // Finish : Specific definition keywords or environment value of authentication (U+Auth)


    // Start : Specific definition keywords or environment value of each service level
    public static final String aId = "authId";
    public static final String accErrMsg = "authErrMsg";
    public static final String accErrCode = "code";
    public static final String authErrCode = "errorCode";
    public static final String resMsg = "responseMsg";

    public static final String mId = "memberId";
    public static final String cType = "contentType";
    public static final String nAId= "nergetAuthId";

    // nergetDomain --> maca-url
    public static final String nDomain= "macaUrl";
    public static final String nPath = "nergetPath";
    public static final String oId = "orderId";
    public static final String lId = "lineId";
    public static final String iId = "identificationKey";
    public static final String tId = "orderType";
    public static final String akId = "client-app-key";
    // Finish : Specific definition keywords or environment value of each service level

    private Integer resStatus = 0;
    private String resJson = "";
    private Properties properties;

    //--------------------------------------------------------------------------
    // DapDa용 환경 변수
    //--------------------------------------------------------------------------
    // 답다 일기 생성 response Body
    //---------------------------------------------------------------------------
    public static final String dId= "id";
    public static final String dMsg = "message";
    public static final String dCreateAt= "createdAt";
    public static final String dupdateAt= "updateAt";
    public static final String ddiaryDate = "diaryDate";
    public static final String dcontent = "content";
    public static final String demotionCategory = "emotionCategory";
    public static final String demotionType = "emotionType";
    public static final String demotionTag = "emotionTag";
    public static final String dmemberId = "memberId";
    public static final String ddiaryPostAIReplyId = "diaryPostAIReplyId";
    public static final String disFirstCreated = "isFirstCreated";
    //----------------------------------------------------------------------------





    public authenticateAndCommunicate() {
    }

    public authenticateAndCommunicate authenticateAndCommunicateForSvc() {
        r = new HashMap<>();

        //TODO :
        readDataMapFromFile(filePath);
        //setValueToMap(nPath, path);
        return this;
    }
    private authenticateAndCommunicate authenticateAndCommunicateForAuth() {
        r = new HashMap<>();
        readDataMapFromFile(filePath);
        return this;
    }

    public static void debugInfo(String... infos) {
        StringBuilder b = new StringBuilder();
        for (String info : infos) {
            b.append(" ").append(info);
        }
        System.out.println(b);
    }

    //TODO :
    public static Boolean isKeyValue(String key) {
        String[] tmp = {aToken, rToken, aId, mId, cType, aVal, aDomain, nAId, nDomain, nPath, oId, lId, iId, tId};

        for (String s : tmp) {
            if (s.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public static authenticateAndCommunicate getBuild(String path, String... pathInfos) {
        authenticateAndCommunicate tmp = new authenticateAndCommunicate().authenticateAndCommunicateForSvc();

        StringBuilder b = new StringBuilder();
        b.append(path);
        for (String info : pathInfos) {
            if (isKeyValue(info)) {
                b.append(tmp.getValueFromMap(info));
            } else {
                b.append(info);
            }
        }
        tmp.setValueToMap(nPath, b.toString());
        return tmp;
    }

    public void readDataMapFromFile(String filePath) {
        properties = new Properties();
        try (FileReader reader = new FileReader(filePath)) {
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getValueFromMap(String key) {
        return properties.getProperty(key);
    }

    public void setValueToMap(String key, String value) {
        properties.setProperty(key, value);
    }

    public void saveChangesToFile() {
        try (FileWriter writer = new FileWriter(filePath)) {
            properties.store(writer, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Integer getResStatus() {
        return this.resStatus;
    }

    private void setResStatus(Integer val) {
        this.resStatus = val;
    }

    private URL setCallUrlInfo() throws MalformedURLException {
        return new URL(getValueFromMap(nDomain) + getValueFromMap(nPath));
    }

    public static boolean isJson(String str) {

        if (str == null) {
            return false;
        }
        if (str.isEmpty()) {
            return false;
        }

        // JSON check
        try {
            JsonElement jsonElement = JsonParser.parseString(str);
            return jsonElement.isJsonObject() || jsonElement.isJsonArray();
        } catch (JsonSyntaxException e) {
            return false;
        }
    }

    private void setResToJson(String s) {
        if (isJson(s)) {
            this.resJson = new GsonBuilder().setPrettyPrinting().create().toJson(JsonParser.parseString(s));

            JsonElement e = JsonParser.parseString(s);

            if (e.isJsonObject()) {
                this.jsonObj = JsonParser.parseString(s).getAsJsonObject();

                System.out.println("---------------------");
                System.out.println("Everything is ok");
                System.out.println("---------------------");
            }
            else if (e.isJsonArray()) {
                System.out.println("---------------------");
                System.out.println("Array Json. Noting To do");
                System.out.println("---------------------");
            }
            else {
                System.out.println("---------------------");
                System.out.println("Occurred Exeption");
                System.out.println("---------------------");
            }
        }
        else {
            this.resJson = s;
        }

        System.out.println("---------------------");
        System.out.println(this.jsonObj);
        System.out.println(this.resJson);
        System.out.println("---------------------");

        this.r.put(resValue, this.jsonObj);
    }
    public JsonObject getResJsonVal() {
        return this.jsonObj;
    }

    public String getResStringVal() {
        return this.resJson;
    }

    public HttpURLConnection setRequestEnv(
            String method, String body, String isMultiPart, String filePath) throws IOException, RuntimeException {
        HttpURLConnection c;
        try {
            c = (HttpURLConnection) this.setCallUrlInfo().openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String boundary = "*****";
        String lineEnd = "\r\n";
        String twoHyphens = "--";

        if (null != isMultiPart && isMultiPart.equals("Y")) {
            c.setDoInput(true);
            c.setUseCaches(false);
            c.setDoOutput(true);
            c.setRequestProperty("Connection", "Keep-Alive");
            c.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

        }
        else {
            c.setRequestProperty("Content-Type", getValueFromMap(cType));
        }
        c.setRequestMethod(method);
        c.setRequestProperty("accept", getValueFromMap(aVal));
        c.setRequestProperty("Authorization", "Bearer " + getValueFromMap(aToken));
        c.setRequestProperty("client-app-key", getValueFromMap(akId));

        if (null != isMultiPart && isMultiPart.equals("Y")) {
            DataOutputStream outputStream = new DataOutputStream(c.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);

            String fileName = new File(filePath).getName();
            outputStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + fileName + "\"" + lineEnd);
            outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
            outputStream.writeBytes(lineEnd);

            FileInputStream fileInputStream = new FileInputStream(filePath);
            int bytesRead;
            byte[] buffer = new byte[4096];
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            fileInputStream.close();
            outputStream.flush();
            outputStream.close();

            authenticateAndCommunicate.debugInfo("DEBUG LOG : Sending Multi Part Files");
        }

        if (body != null && (methodPost.equals(method) || methodPut.equals(method))) {
            c.setDoOutput(true);

            byte[] requestBodyBytes = body.getBytes(StandardCharsets.UTF_8);
            c.setRequestProperty("Content-Length", String.valueOf(requestBodyBytes.length));
        }
        return c;
    }

    private String needToGenForBodyWithEnv(String method, String body, String svcName) {

        if (svcName.equals("nerget")) {
            // TODO : 범용성을 위해 너겟 특화 코드를 분기 처리 함. (향후 유지보수 필요 함)
            if (body != null && (method.equals(authenticateAndCommunicate.methodPost) || method.equals(authenticateAndCommunicate.methodPut))) {
                JsonElement jsonElement = JsonParser.parseString(body);

                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();

                    for (String key : jsonObject.keySet()) {
                        if (key.equals(authenticateAndCommunicate.mId)
                                || key.equals(authenticateAndCommunicate.oId)
                                || key.equals(authenticateAndCommunicate.lId)
                                || key.equals(authenticateAndCommunicate.iId)
                                || key.equals(authenticateAndCommunicate.tId)) {
                            jsonObject.addProperty(key, getValueFromMap(key));
                        }
                    }
                    return jsonObject.toString();
                }
                return body;
            }
        }
        return body;
    }

    public HashMap<String, JsonObject> checkResultsForRequest(String whichType, Integer passValue, Integer resCode, Integer expectValue, String resMsg, String expectMsg) {
        if (!whichType.equals("001")) {
            if (resCode.equals(HttpURLConnection.HTTP_OK) || (resCode >= HttpURLConnection.HTTP_INTERNAL_ERROR)) {
                return this.r;
            }
            else if (expectValue.equals(resCode)) {
                return new HashMap<>();
            }
        }
        else {
            if (passValue.equals(resCode)) {
                return new HashMap<>();
            }
            else if (expectMsg.equals(resMsg)) {
                if (resCode.equals(expectValue)) {
                    return new HashMap<>();
                }
                return this.r;
            }
        }
        return this.r;
    }

    public void checkContainsThenSetUp(String reponse, String... substrings) {
        for (String substr : substrings) {
            if (reponse.contains(substr)) {
                setValueToMap(substr, getResJsonVal().get(substr).getAsString());
            }
        }
    }

    private Boolean isThisAbleToContinue(Integer target, Integer ... diffValues) {

        for (Integer info : diffValues) {
            if (target.equals(info)) {
                return false;
            }
        }
        return true;
    }

    public authenticateAndCommunicate doRequestWithGen(String method, String body, String isMultiPart, String filePath, String svcName) {
        try {
            String realBody = needToGenForBodyWithEnv(method, body, svcName);
            HttpURLConnection con = setRequestEnv(method, realBody, isMultiPart, filePath);

            if (realBody != null && (methodPost.equals(method) || methodPut.equals(method))) {
                byte[] msg = realBody.getBytes(StandardCharsets.UTF_8);
                con.getOutputStream().write(msg);
            }

            setResStatus(con.getResponseCode());

            authenticateAndCommunicate.debugInfo("DEBUG LOG : Request Info List ", this.properties.getProperty(nPath), method, ",", realBody, ",", isMultiPart, ",", filePath, ",", this.getResStatus().toString());

            StringBuilder content = new StringBuilder();
            String inputLine;

            if (isThisAbleToContinue(con.getResponseCode(), HttpURLConnection.HTTP_UNAUTHORIZED, HttpURLConnection.HTTP_CONFLICT)) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                (HttpURLConnection.HTTP_OK != con.getResponseCode()) ?
                                        con.getErrorStream() == null ?
                                                con.getInputStream()
                                                : con.getErrorStream()
                                        : con.getInputStream()
                        )
                );
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
            }
            con.disconnect();

            if (getResStatus() > LIMIT_RES_STATUS_VAL) {
                setResToJson(HttpURLConnection.HTTP_UNAUTHORIZED != con.getResponseCode() ? content.toString() : "Occurred - UnAuthorized");
                return this;
            }
            else if (content.toString().isEmpty()) {
                authenticateAndCommunicate.debugInfo("DEBUG LOG : Response Json Data is Null( ", this.getResStringVal(), "), RET is ", this.getResStatus().toString());
                authenticateAndCommunicate.debugInfo("DEBUG LOG : ", content.toString());
                setResToJson(HttpURLConnection.HTTP_UNAUTHORIZED == con.getResponseCode() ? "Occurred - UnAuthorized" : content.toString());
                return this;
            }
            setResToJson(HttpURLConnection.HTTP_UNAUTHORIZED == con.getResponseCode() ? "Occurred - UnAuthorized" : content.toString());
            authenticateAndCommunicate.debugInfo("DEBUG LOG : Response Json Data : ", this.getResStringVal(), " : RET : ", this.getResStatus().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (this.getResStatus().equals(HttpURLConnection.HTTP_OK )) {
            if (method.equals(authenticateAndCommunicate.methodPost) || method.equals(authenticateAndCommunicate.methodPut)) {
                checkContainsThenSetUp(getResStringVal(), oId, lId, iId, tId);
            }
            checkContainsThenSetUp(getResStringVal(), dId, dMsg);
        }

        if (getResJsonVal() != null && getResJsonVal().has(accErrCode)) {
            setValueToMap(resMsg, getResJsonVal().get(accErrCode).getAsString());
        }
        saveChangesToFile();
        return this;
    }

    private String makeBodyForRefreshToken() {
        return "{ \n" + "\"accessToken\": \"" + getValueFromMap(aToken) + "\"" + ",\n" + "\"refreshToken\": \"" + getValueFromMap(rToken) + "\"" + "\n" + "}";
    }
    private URL setCallUrlInfoForAuth() throws MalformedURLException {
        return new URL(getValueFromMap(aDomain));
    }
    public HttpURLConnection setReqEnvForAuth() throws IOException, RuntimeException {
        HttpURLConnection c;
        try {
            c = (HttpURLConnection) this.setCallUrlInfoForAuth().openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        c.setRequestMethod(authenticateAndCommunicate.methodPost);
        c.setRequestProperty("Content-Type", getValueFromMap(cType));
        c.setRequestProperty("Accept", getValueFromMap(aVal));
        c.setRequestProperty("Client-Id", getValueFromMap(nAId));
        c.setRequestProperty("Content-Length", String.valueOf(makeBodyForRefreshToken().getBytes(StandardCharsets.UTF_8).length));
        //c.setRequestProperty("Authorization", "Bearer " + getValueFromMap(aToken));

        c.setDoOutput(true);

        return c;
    }

    public HashMap<String, JsonObject> checkResultsForAuth(Integer passValue, Integer resCode, Integer expectValue, String resMsg, String expectMsg) {

        if (passValue.equals(resCode)) {
            return (expectMsg.equals(resMsg)) ? new HashMap<>() : this.r;
        }
        else {
            if (expectMsg.equals(resMsg)) {
                if (resCode.equals(expectValue)) {
                    return new HashMap<>();
                }
                return this.r;
            }
            return this.r;
        }
    }

    public authenticateAndCommunicate doRequestWithGenForAuth() {
        try {
            HttpURLConnection con = setReqEnvForAuth();
            con.getOutputStream().write(makeBodyForRefreshToken().getBytes(StandardCharsets.UTF_8));

            authenticateAndCommunicate.debugInfo("DEBUG LOG : Request Info List ",
                    authenticateAndCommunicate.methodPost, ",", makeBodyForRefreshToken(), ",", filePath);

            setResStatus(con.getResponseCode());

            StringBuilder content = new StringBuilder();
            String inputLine;
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            (HttpURLConnection.HTTP_OK  != con.getResponseCode()) ?
                                    con.getErrorStream() == null ?
                                            con.getInputStream()
                                            : con.getErrorStream()
                                    : con.getInputStream()
                    )
            );
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            setResToJson(content.toString());

            authenticateAndCommunicate.debugInfo("DEBUG LOG : Response Json Data : ", this.getResStringVal(), " : RET : ", this.getResStatus().toString());

            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(this.getResStatus().equals(HttpURLConnection.HTTP_OK )) {
            setValueToMap(aToken, getResJsonVal().get(aToken).getAsString());
            setValueToMap(rToken, getResJsonVal().get(rToken).getAsString());
        }
        else {
            setValueToMap(accErrMsg, getResJsonVal().get(authErrCode).getAsString());
        }
        saveChangesToFile();
        return this;
    }
    public static authenticateAndCommunicate getBuildForAuth() {
        return new authenticateAndCommunicate().authenticateAndCommunicateForAuth();
    }
}
