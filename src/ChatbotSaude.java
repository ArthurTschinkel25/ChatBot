import java.text.Normalizer;
import java.util.Scanner;

public class ChatbotSaude {

    private static final int ESTADO_NORMAL = 0;
    private static final int ESTADO_AGUARDANDO_ESPECIALIDADE = 1;
    private static final int ESTADO_AGUARDANDO_CONFIRMACAO_CANCELAMENTO = 2;

    private static int estado = ESTADO_NORMAL;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        System.out.println("=================================================");
        System.out.println(" Assistente Virtual - Clinica Saude+");
        System.out.println("=================================================");
        System.out.println("Bot: Ola! Eu sou o assistente virtual da Clinica Saude+.");
        System.out.println("Bot: Digite 'sair', 'tchau' ou 'encerrar' para finalizar a conversa.");
        System.out.println();

        while (continuar) {
            System.out.print("Voce: ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String entrada = scanner.nextLine();
            String mensagem = normalizar(entrada);

            if (contemPalavra(mensagem, "sair") || contemPalavra(mensagem, "tchau")
                    || contemPalavra(mensagem, "encerrar") || contemPalavra(mensagem, "adeus")) {
                System.out.println("Bot: Foi um prazer, tchau!");
                continuar = false;
                continue;
            }

            String resposta = processarMensagem(mensagem);
            System.out.println("Bot: " + resposta);
        }

        scanner.close();
    }

    private static String normalizar(String texto) {
        String semAcento = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        String minuscula = semAcento.toLowerCase().trim();
        String semPontuacao = minuscula.replaceAll("[^a-z0-9\\s]", " ");
        return semPontuacao.replaceAll("\\s+", " ").trim();
    }

    private static boolean contemPalavra(String mensagem, String palavra) {
        return mensagem.contains(palavra);
    }

    private static String processarMensagem(String msg) {

        if (estado == ESTADO_AGUARDANDO_ESPECIALIDADE) {
            estado = ESTADO_NORMAL;
            return "Consulta de " + msg + " agendada com sucesso! Voce recebera uma confirmacao por e-mail. "
                    + "Posso ajudar em algo mais?";
        }

        if (estado == ESTADO_AGUARDANDO_CONFIRMACAO_CANCELAMENTO) {
            estado = ESTADO_NORMAL;
            if (contemPalavra(msg, "sim")) {
                return "Sua consulta foi cancelada com sucesso.";
            } else {
                return "Ok, sua consulta foi mantida.";
            }
        }

        if (contemPalavra(msg, "convenio") || contemPalavra(msg, "plano")) {
            return "Aceitamos os principais convenios: Unimed, Cassems e atendimento particular.";
        }

        if ((contemPalavra(msg, "dor") && contemPalavra(msg, "peito"))
                || contemPalavra(msg, "emergencia") || contemPalavra(msg, "urgente")
                || contemPalavra(msg, "socorro")) {
            return "Emergencia !!!! Ligue para o SAMU (192)";
        }

        if (contemPalavra(msg, "febre") && (contemPalavra(msg, "tosse") || contemPalavra(msg, "garganta"))) {
            return "Sintomas similares com gripe, agenda uma consulta com um clínico geral"
                    + "Deseja agendar?";
        }

        if (contemPalavra(msg, "dor") && contemPalavra(msg, "cabeca")) {
            return "Mantenha-se hidratado para possível melhora, caso não melhore procure um neurologista";
        }

        if (contemPalavra(msg, "cancelar") || contemPalavra(msg, "desmarcar") || contemPalavra(msg, "cancelamento")) {
            estado = ESTADO_AGUARDANDO_CONFIRMACAO_CANCELAMENTO;
            return "Voce tem certeza que deseja cancelar sua consulta? (sim/nao)";
        }

        if (contemPalavra(msg, "agendar") || contemPalavra(msg, "marcar") || contemPalavra(msg, "consulta")) {
            estado = ESTADO_AGUARDANDO_ESPECIALIDADE;
            return "Claro! Qual especialidade voce deseja agendar a consulta? (ex: cardiologia, pediatria, clinico geral)";
        }

        if (contemPalavra(msg, "horario") || contemPalavra(msg, "funcionamento") || contemPalavra(msg, "aberto")) {
            return "Funcionamos de segunda a sexta, das 7h as 19h, e aos sabados das 8h as 12h.";
        }

        if (contemPalavra(msg, "endereco") || contemPalavra(msg, "localizacao") || contemPalavra(msg, "local")) {
            return "Estamos localizados na Rua radio clube, 123, Centro.";
        }

        if (contemPalavra(msg, "exame") || contemPalavra(msg, "resultado")) {
            return "Os resultados de exames ficam disponiveis no portal do paciente em ate 3 dias uteis.";
        }

        if (contemPalavra(msg, "obrigado") || contemPalavra(msg, "obrigada") || contemPalavra(msg, "valeu")) {
            return "Por nada! Estou aqui para ajudar sempre que precisar.";
        }

        if (contemPalavra(msg, "ola") || contemPalavra(msg, "oi") || contemPalavra(msg, "bom dia")
                || contemPalavra(msg, "boa tarde") || contemPalavra(msg, "boa noite")) {
            return "Ola! Como posso ajudar voce hoje? Posso marcar consultas, informar horarios, endereco e convenios.";
        }

        return "Desculpe, nao entendi sua mensagem. Voce pode perguntar sobre: agendamento, cancelamento, "
                + "horarios, endereco, convenios, exames ou sintomas.";
    }
}