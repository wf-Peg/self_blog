package com.pwf.test;

import com.pwf.util.Md5Util;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestApplicationTests {
    @Test
    public void testGetUrl() throws UnsupportedEncodingException {
        //创建httpClient、HttpResponse
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        //第一步
        long timestemp = System.currentTimeMillis();
//        JSONObject datajson = new JSONObject();
////        Map<String ,String> datajson=new HashMap<>();
//        datajson.put("appKey", "H5_4282");
//        datajson.put("windowId", "2517404400_117");
        String data = "{\"appKey\":\"H5_4282\",\"windowId\":\"2517404400_117\"}";
        data = URLEncoder.encode(data, "utf-8");
//        String data=new String(datajson.toString().getBytes(),"utf-8");
//        String data = datajson.toString();
//        String url = "https://www.zhiboshuju.com/product/item";
        String sign = Md5Util.md5("&" + timestemp + "&" + "12574478" + "&" + data);
        System.out.println("sign=" + sign);
        String url = "https://h5api.m.taobao.com/h5/mtop.mediaplatform.anchor.info/1.0/?" +
                "jsv=2.4.8&appKey=12574478&t=" + timestemp + "&sign=" + sign + "&api=mtop.mediaplatform.anchor.info" +
                "&v=1.0&AntiCreep=true&AntiFlood=true&type=jsonp&dataType=jsonp&" +
                "callback=mtopjsonp2&data=" + data + "";
        //发起get连接
        HttpGet request = new HttpGet(url);
        System.out.println(url);
        try {
            //设置请求头
            request.addHeader("accept", "*/*");
            request.addHeader("Host", "h5api.m.taobao.com");
            request.addHeader("Content-Type", "application/json;charset=UTF-8");
            request.addHeader("user-agent",
                    "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) " +
                            "AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
            //执行请求
            response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            System.out.println("--------------");
            // 打印响应内容
//            if (entity != null) {
//                System.out.println("Response content length" + entity.getContentLength());
//                System.out.println("Response content" + entity.getContent());
//                System.out.println("Response Contentype" + entity.getContentType());
//                System.out.println("Response ContenEncoding" + entity.getContentEncoding());
//            }
//            System.out.println("--------------");
            //打印所有头信息
            Header[] hr = response.getAllHeaders();
            for (int i = 0; i < hr.length; i++) {
                Header header1 = hr[i];
                System.out.println(header1.getName() + ":" + header1.getValue());
            }
            System.out.println("--------------");
//            String response = EntityUtils.toString(httpEntity, "utf-8");
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                System.out.println("响应信息：" + result);
                Header[] headers = response.getHeaders("Set-Cookie");
//                StringBuilder zb_cookies = new StringBuilder();
                String _m_h5_tk = "";
                String _m_h5_tk_enc = "";
                //获取cookies头
                for (Header header : headers) {
//                    if (!header.getValue().contains("_tb_token_")) {
                    String cookies1 = header.getValue().split(";")[0];
                    if (null != cookies1 && !"".equals(cookies1.trim())) {
                        if (cookies1.contains("_m_h5_tk") || cookies1.contains("_m_h5_tk_enc")) {
                            String cookie = cookies1.split("=")[1];
                            if (cookies1.contains("_m_h5_tk")) {
                                _m_h5_tk = cookie;
                            }
                            if (cookies1.contains("_m_h5_tk_enc")) {
                                _m_h5_tk_enc = cookie;
                            }
                            System.out.println(cookies1 + ";");
//                            zb_cookies.append(cookies1+";");
                        }
//                            System.out.println(cookies1);
//                        }
                    }
                }
                String zb_cookies = "_m_h5_tk_enc=" + _m_h5_tk_enc + ";_m_h5_tk=" + _m_h5_tk + ";";
                System.out.println("zb_cookies=" + zb_cookies);
                //第二步
                System.out.println("=====");
                System.out.println("_m_h5_tk=" + _m_h5_tk);
                String sign_str = _m_h5_tk + "&" + timestemp + "&" + "24679788"+"&";
                System.out.println("sign_str=" + sign_str);
                String sign_one = Md5Util.md5(sign_str);
                System.out.println("sign_one=" + sign_one);
                System.out.println("=====");
                String accountName = "渝妹子儿";
                String data_json = "{\"searchID\":\"" + sign_one + "\",\"s\":\"0\",\"n\":\"10\",\"CREATOR\":\"{}\"," +
                        "\"PARCELABLE_WRITE_RETURN_VALUE\":\"1\",\"searchKey\":\"" + accountName + "\"," +
                        "\"type\":\"accountLive\",\"CONTENTS_FILE_DESCRIPTOR\":\"1\"}";
                System.out.println("data_json=" + data_json);
                String md5Sign2 = Md5Util.md5(_m_h5_tk + "&" + timestemp + "&24679788&" + data_json);
                System.out.println("sign(md5Sign2)="+md5Sign2);
                String data2 = URLEncoder.encode(data_json, "utf-8");
                System.out.println("data(data2)="+data2);
                String url2 = "https://h5api.m.taobao.com/h5/mtop.mediaplatform.live.searchv2/1.0/?jsv=2.4.2" +
                        "&appKey=24679788&t=" + timestemp + "&sign=" + md5Sign2 + "&api=mtop.taobao.powermsg.h5.msg.pullnativemsg" +
                        "&v=1.0&type=jsonp&dataType=jsonp&callback=mtopjsonp9&data=" + data2;
                request = new HttpGet(url2);
                request.addHeader("Accept", "*/*");
                request.addHeader("cookie", zb_cookies);
                request.addHeader("Content-Type", "application/json;charset=UTF-8");
                request.addHeader("user-agent", "Mozilla/5.0 (Windows; U; Windows NT 5.2; en-US) " +
                        "AppleWebKit/532.9 (KHTML, like Gecko) Chrome/5.0.310.0 Safari/532.9");
                response = httpClient.execute(request);
                entity = response.getEntity();
                result = EntityUtils.toString(entity);
                System.out.println("第二步响应信息：" + result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
