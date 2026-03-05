package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import com.example.model.Produto;
import com.example.model.ProdutoNaoPerecivel;
import com.example.model.ProdutoPerecivel;

public class Comercio {
    /** Para inclusão de novos produtos no vetor */
    static final int MAX_NOVOS_PRODUTOS = 10;

    /**
     * Nome do arquivo de dados. O arquivo deve estar localizado na raiz do projeto
     */
    static String nomeArquivoDados;

    /** Scanner para leitura do teclado */
    static Scanner teclado;

    /**
     * Vetor de produtos cadastrados. Sempre terá espaço para 10 novos produtos a
     * cada execução
     */
    static Produto[] produtosCadastrados;

    /** Quantidade produtos cadastrados atualmente no vetor */
    static int quantosProdutos;

    /** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
    static void pausa() {
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    /** Cabeçalho principal da CLI do sistema */
    static void cabecalho() {
        System.out.println("AEDII COMÉRCIO DE COISINHAS");
        System.out.println("===========================");
    }

    /**
     * Imprime o menu principal, lê a opção do usuário e a retorna (int).
     * Perceba que poderia haver uma melhor modularização com a criação de uma
     * classe Menu.
     * 
     * @return Um inteiro com a opção do usuário.
     */
    static int menu() {
        cabecalho();
        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar e listar um produto");
        System.out.println("3 - Cadastrar novo produto");
        System.out.println("0 - Sair");
        System.out.print("Digite sua opção: ");
        return Integer.parseInt(teclado.nextLine());
    }

    /**
     * Lê os dados de um arquivo texto e retorna um vetor de produtos. Arquivo no
     * formato
     * N (quantiade de produtos) <br/>
     * tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade] <br/>
     * Deve haver uma linha para cada um dos produtos. Retorna um vetor vazio em
     * caso de problemas com o arquivo.
     * 
     * @param nomeArquivoDados Nome do arquivo de dados a ser aberto.
     * @return Um vetor com os produtos carregados, ou vazio em caso de problemas de
     *         leitura.
     */
    static Produto[] lerProdutos(String nomeArquivoDados) {
        Produto[] vetorProdutos = null;
        try {
            File arquivo = new File(nomeArquivoDados);
            Scanner leitor = new Scanner(arquivo, Charset.forName("UTF-8"));

            if (leitor.hasNextLine()) {
                int quantidadeProdutos = Integer.parseInt(leitor.nextLine());
                vetorProdutos = new Produto[quantidadeProdutos + MAX_NOVOS_PRODUTOS];
                quantosProdutos = 0;

                for (int i = 0; i < quantidadeProdutos && leitor.hasNextLine(); i++) {
                    String linha = leitor.nextLine();
                    try {
                        // Tenta criar o produto
                        vetorProdutos[quantosProdutos] = Produto.criarDoTexto(linha);
                        quantosProdutos++;
                    } catch (IllegalArgumentException e) {
                        // Se a data estiver vencida, o construtor joga o erro aqui
                        // O programa apenas avisa e pula para o próximo produto do arquivo
                        System.out.println("Aviso: Pulando produto vencido no arquivo -> " + linha);
                    }
                }
            } else {
                System.out.println("Arquivo vazio: " + nomeArquivoDados);
            }
            leitor.close();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado: " + nomeArquivoDados);
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + nomeArquivoDados);
        }
        return vetorProdutos;
    }

    /** Lista todos os produtos cadastrados, numerados, um por linha */
    static void listarTodosOsProdutos() {
        cabecalho();
        System.out.println("\nPRODUTOS CADASTRADOS:");
        for (int i = 0; i < produtosCadastrados.length; i++) {
            if (produtosCadastrados[i] != null)
                System.out.println(String.format("%02d - %s", (i + 1), produtosCadastrados[i].toString()));
        }
    }

    /**
     * Localiza um produto no vetor de cadastrados, a partir do nome, e imprime seus
     * dados.
     * A busca não é sensível ao caso. Em caso de não encontrar o produto, imprime
     * mensagem padrão
     */
    static void localizarProdutos() {
        System.out.print("Digite o nome (descrição) do produto: ");
        String busca = teclado.nextLine();

        // Criamos um objeto temporário para usar o equals que você implementou
        // (Pois seu equals recebe um Object e faz cast para Produto)
        Produto temporario = new ProdutoNaoPerecivel(busca, 1, 0.1, 1);
        boolean encontrado = false;

        for (int i = 0; i < quantosProdutos; i++) {
            if (produtosCadastrados[i].equals(temporario)) {
                System.out.println("Produto encontrado: " + produtosCadastrados[i]);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            System.out.println("Produto não localizado.");
        }
    }

    /**
     * Rotina de cadastro de um novo produto: pergunta ao usuário o tipo do produto,
     * lê os dados correspondentes,
     * cria o objeto adequado de acordo com o tipo, inclui no vetor. Este método
     * pode ser feito com um nível muito
     * melhor de modularização. As diversas fases da lógica poderiam ser
     * encapsuladas em outros métodos.
     * Uma sugestão de melhoria mais significativa poderia ser o uso de padrão
     * Factory Method para criação dos objetos.
     */
static void cadastrarProduto() {
    if (quantosProdutos >= produtosCadastrados.length) {
        System.out.println("Erro: Limite do vetor atingido.");
        return;
    }

    try {
        System.out.println("Tipo de produto: (1) Não Perecível | (2) Perecível");
        int tipo = Integer.parseInt(teclado.nextLine());

        System.out.print("Descrição: ");
        String desc = teclado.nextLine();
        
        System.out.print("Preço de custo: ");
        double custo = Double.parseDouble(teclado.nextLine().replace(",", "."));

        System.out.print("Margem de lucro (Enter para padrão 20%): ");
        String margemInput = teclado.nextLine().replace(",", ".");

        System.out.print("Quantidade em estoque ");
        int quantidadeInput = Integer.parseInt(teclado.nextLine().replace(",", "."));
        
        Produto novo = null;

        if (tipo == 1) {
            // Se o input da margem estiver vazio, usa o construtor da Margem Padrão
            if (margemInput.isEmpty()) {
                novo = new ProdutoNaoPerecivel(desc, custo, quantidadeInput);
            } else {
                double margem = Double.parseDouble(margemInput);
                novo = new ProdutoNaoPerecivel(desc, custo, margem, quantidadeInput);
            }
        } else if (tipo == 2) {
            System.out.print("Data de validade (dd/mm/aaaa): ");
            String dataStr = teclado.nextLine();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate validade = LocalDate.parse(dataStr, formato);

            if (margemInput.isEmpty()) {
                // Para o Perecível, como o construtor padrão geralmente exige a data,
                // passamos 0.2 (ou o valor da sua constante MARGEM_PADRAO) manualmente
                novo = new ProdutoPerecivel(desc, custo, 0.2, validade, quantidadeInput);
            } else {
                double margem = Double.parseDouble(margemInput);
                novo = new ProdutoPerecivel(desc, custo, margem, validade, quantidadeInput);
            }
        }

        if (novo != null) {
            produtosCadastrados[quantosProdutos] = novo;
            quantosProdutos++;
            salvarProdutos(nomeArquivoDados);
            System.out.println("Produto cadastrado com sucesso!");
        }

    } catch (NumberFormatException e) {
        System.out.println("Erro: Valor numérico inválido.");
    } catch (java.time.format.DateTimeParseException e) {
        System.out.println("Erro: Formato de data inválido. Use dd/mm/aaaa.");
    } catch (IllegalArgumentException e) {
        System.out.println("Erro no cadastro: " + e.getMessage());
    }
}

    /**
     * Salva os dados dos produtos cadastrados no arquivo csv informado. Sobrescreve
     * todo o conteúdo do arquivo.
     * 
     * @param nomeArquivo Nome do arquivo a ser gravado.
     */
    public static void salvarProdutos(String nomeArquivo) {
        try {
            FileWriter escritor = new FileWriter(nomeArquivo, Charset.forName("UTF-8"));

            // Escreve a quantidade de produtos atual
            escritor.write(quantosProdutos + "\n");

            // Escreve os dados de cada produto
            for (int i = 0; i < quantosProdutos; i++) {
                escritor.write(produtosCadastrados[i].gerarDadosTexto() + "\n");
            }

            escritor.close();
            System.out.println("Dados salvos com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar os produtos: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        teclado = new Scanner(System.in, Charset.forName("ISO-8859-2"));
        nomeArquivoDados = "dadosProdutos.csv";
        produtosCadastrados = lerProdutos(nomeArquivoDados);
        int opcao = -1;
        do {
            opcao = menu();
            switch (opcao) {
                case 1 -> listarTodosOsProdutos();
                case 2 -> localizarProdutos();
                case 3 -> cadastrarProduto();
            }
            pausa();
        } while (opcao != 0);

        salvarProdutos(nomeArquivoDados);
        teclado.close();
    }
}
