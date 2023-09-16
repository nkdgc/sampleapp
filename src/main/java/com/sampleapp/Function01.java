package com.sampleapp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.io.IOException;
import java.io.PrintWriter;
// import java.io.File;
// import java.io.FileWriter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// import java.util.Date;
import java.util.ArrayList;
import java.util.List;
// import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("serial")
@WebServlet("/function01")
public class Function01 extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(Function01.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        logger.info("start function01");

        // // 現在時刻を取得
        // Date nowDate = new Date();
        // SimpleDateFormat sdfForFileName = new SimpleDateFormat("yyyy-MMdd-HHmm-ss.SSS");
        // String formatNowDate = sdfForFileName.format(nowDate);

        // // DBからレコード取得
        // List<String> records = getRecordFromDB();

        // // g1app アクセス
        // String g1app_res = getOutputFromG1app();

        // // corticon-server アクセス
        // String corticon_server_res = getOutputFromCorticonServer();

        // // Response/NFS出力コンテンツ作成
        // StringBuilder sb = new StringBuilder();
        // sb.append("[Function]\ng2app/function01\n\n");
        // sb.append("[Date]\n");
        // sb.append(formatNowDate);
        // sb.append("\n\n");
        // sb.append("[Records from PostgreSQL]\n");
        // sb.append(String.join("\n",records));
        // sb.append("\n\n[Output of G1app]\n");
        // sb.append(g1app_res);
        // sb.append("\n[Output of Corticon-Server]\n");
        // sb.append(corticon_server_res);
        // sb.append("\n");
        // String resTxt = sb.toString();

        // // NFS上にファイル出力
        // try{
        //   File file = new File(String.join("", "/nfs/function01-", formatNowDate, ".txt"));
        //   FileWriter filewriter = new FileWriter(file);
        //   filewriter.write(resTxt);
        //   filewriter.close();
        // }catch(IOException e){
        //   System.out.println(e);
        // }

        // // http応答
        // PrintWriter writer = resp.getWriter();
        // writer.println(resTxt);
        // writer.close();

        // http応答
        PrintWriter writer = resp.getWriter();
        writer.println("response from function01");
        writer.close();
    }

    private List<String> getRecordFromDB(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        List<String> res = new ArrayList<String>();

        // 環境変数から接続先データ取得
        // DB_URL 例:"jdbc:postgresql://10.191.194.221:5432/g2db"
        String url = System.getenv("DB_URL");
        // DB_USER 例:"g2app"
        String user = System.getenv("DB_USER");
        // DB_PW 例:"VMware1!"
        String password = System.getenv("DB_PW");

        try{
            // DBからレコード取得
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.createStatement();
            String sql = "select content from g2table order by id;";
            rset = stmt.executeQuery(sql);

            while(rset.next()){
                res.add(rset.getString(1));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            try {
                if(rset != null)rset.close();
                if(stmt != null)stmt.close();
                if(conn != null)conn.close();
            }
            catch (SQLException e){
                e.printStackTrace();
            }
            return res;
        }
    }

    private String getOutputFromG1app(){
        return sendGetRequestByHttp(System.getenv("G1APP_URL"));
    }

    private String getOutputFromCorticonServer(){
        return sendGetRequestByHttp(System.getenv("CORTICON_SERVER_URL"));
    }

    private String sendGetRequestByHttp(String url){
        String res = null;

        try {
          //HTTPクライアント生成
          HttpClient client = HttpClient.newHttpClient();
          //リクエスト準備
          HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();
          //レスポンス取得
          res = client.send(req, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
          e.printStackTrace();
        }
        return res;
    }
}

