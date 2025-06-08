package br.com.alura.conversor.modelos;
import java.io.*;

public class LoadText extends DataRequest {
    public String fileReader() throws FileNotFoundException {
        String filePath = "moedas.txt";
        String fileContent = readFileToString(filePath);

        IniFileCreate(); //cria arquivo de configuração da API key
        ReadIniFile(); // faz a leitura de api_set

        if (fileContent != null) {
            System.out.println("Arquivo encontrado\n");
        } else {
            System.out.println("Falha ao ler o arquivo.\n");
            PrintWriter writer = new PrintWriter("moedas.txt");

            writer.println("""  
                    ######################################################
                    Por favor, Selecione a moeda que deseja converter :
                    1 - BRL - Real Brasileiro
                    2 - USD - Dolar
                    3 - EUR - Euro
                    4 - ARS - Peso Argentino
                    5 - COP - Peso Colombiano
                    6 - CAD - Dolar Canadense
                    7 - MXN - Peso Mexicano
                    8 - CLP - Peso Chileno
                    
                    0 - Sair
                    ######################################################
                    """
            );
            writer.println("""
                    ########################################################
                    Convert: %s, to the following currencies :
                    Por favor, Selecione a moeda que deseja converter :
                    1 - BRL - Real Brasileiro
                    2 - USD - Dolar
                    3 - EUR - Euro
                    4 - ARS - Peso Argentino
                    5 - COP - Peso Colombiano
                    6 - CAD - Dolar Canadense
                    7 - MXN - Peso Mexicano
                    8 - CLP - Peso Chileno
                    
                    9 - Voltar
                    0 - Sair
                    ########################################################
                    """);
            writer.close();
            System.out.println("arquivo criado com successo.\n");
        }
        return fileContent;
    }

    public static String readFileToString(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("//") && (!line.startsWith("["))) {
                    contentBuilder.append(line).append("\n");
                }
            }
        } catch (FileNotFoundException f) {
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return contentBuilder.toString();
    }
}

