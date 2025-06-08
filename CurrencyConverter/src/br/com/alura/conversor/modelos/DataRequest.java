package br.com.alura.conversor.modelos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

public class DataRequest {

    private String currencyFrom;
    private String currencyTo;
    private String api_set;
    public double result;

    public DataRequest() {

    }

    public void setCurrencyTo(String currencyTo) {
        this.currencyTo = currencyTo;
    }

    public void setCurrencyFrom(String currencyFrom) {
        this.currencyFrom = currencyFrom;
    }

    public String ReadIniFile(){
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("API.ini")) {
            props.load(in);
           // String value = props.getProperty("api_set");
        } catch (IOException e) {
            System.out.println("Não foi possivel ler api.ini");
        }
        return props.getProperty("api_set");
    }
    public String convertCurrency (Double amount) throws IOException, InterruptedException {
        String url_ = "https://v6.exchangerate-api.com/v6/";
        api_set = ReadIniFile();
        String url_convert = url_ + api_set + "/pair/";
        String convertCurrency = url_convert + "/" + currencyFrom + "/" + currencyTo + "/" + amount.toString();
        System.out.println(convertCurrency);
        return httpRequest(convertCurrency);
    }

    public String httpRequest(String address) throws IOException, InterruptedException {
        Gson gson = new GsonBuilder()
                //.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .create();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(address))
                .build();
        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());
        String json = response.body();
        Format x = gson.fromJson(json, Format.class); //recebe o json da api e transforma no formato desejado com apenas os dados a serem coletados.
        DataRequest dataRequest = new DataRequest(x); //transforma na classe datarequest e retorna como string (deve haver um jeito mais inteligente de fazer isso)
        return dataRequest.toString();
    }

    public DataRequest (Format format){
        this.currencyFrom = format.base_code();
        this.currencyTo = format.target_code();
        this.result = format.conversion_result();
    }

    public void IniFileCreate (){
        String filePath = "API.ini";
        File iniFile = new File(filePath);

        try {
            if (iniFile.createNewFile()) {
                System.out.println("API.ini criado com successo: " + iniFile.getName());
                WriteIniFile(); //insere a variavel api_set
            } else {
               // System.out.println("API.ini já existe.");
            }
        } catch (IOException e) {
            System.err.println("Erro ao criar api.ini: " + e.getMessage());
        }
    }
    //"a41ad926db9030108c3c54db";
    public void WriteIniFile(){
        Properties props = new Properties();
        props.setProperty("api_set", "");

        try (FileOutputStream fos = new FileOutputStream("API.ini")) {
            props.store(fos, "Insira sua API KEY");
            System.out.println("api_set armazenado em API.ini.");
        } catch (IOException e) {
            System.err.println("Erro ao escrever API.ini: " + e.getMessage());
        }
    }
    @Override
    public String toString() {
        return "Valor de : "+ this.currencyFrom + " to "+ this.currencyTo + " Corresponde a = " + this.result;
    }
}
