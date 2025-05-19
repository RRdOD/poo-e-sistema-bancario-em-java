import lombok.Data;
import lombok.NonNull;

// Classe abstrata que representa uma conta bancária genérica
@Data
public abstract class Conta {
    @NonNull protected String numero;
    @NonNull protected String agencia;
    @NonNull protected Cliente cliente;
    protected double saldo;
    
    public Conta(String numero, String agencia, Cliente cliente, double saldoInicial) {
        this.numero = numero;
        this.agencia = agencia;
        this.cliente = cliente;
        this.saldo = saldoInicial;
    }
    
    // Métodos abstratos que devem ser implementados pelas subclasses
    public abstract void sacar(double valor);
    public abstract void depositar(double valor);
    
    // Método concreto para transferência
    public void transferir(Conta destino, double valor) {
        if (this.saldo >= valor) {
            this.sacar(valor);
            destino.depositar(valor);
            System.out.println("Transferência realizada com sucesso!");
        } else {
            System.out.println("Saldo insuficiente para transferência.");
        }
    }
    
    // Método para consultar saldo
    public void consultarSaldo() {
        System.out.printf("Saldo atual: R$ %.2f%n", this.saldo);
    }
}

// Classe ContaCorrente que herda de Conta
@Data
public class ContaCorrente extends Conta {
    private double limiteChequeEspecial;
    
    public ContaCorrente(String numero, String agencia, Cliente cliente, double saldoInicial, double limiteChequeEspecial) {
        super(numero, agencia, cliente, saldoInicial);
        this.limiteChequeEspecial = limiteChequeEspecial;
    }
    
    @Override
    public void sacar(double valor) {
        double saldoDisponivel = this.saldo + this.limiteChequeEspecial;
        if (valor <= saldoDisponivel) {
            this.saldo -= valor;
            System.out.println("Saque realizado com sucesso!");
        } else {
            System.out.println("Saldo insuficiente (incluindo cheque especial).");
        }
    }
    
    @Override
    public void depositar(double valor) {
        if (valor > 0) {
            this.saldo += valor;
            System.out.println("Depósito realizado com sucesso!");
        } else {
            System.out.println("Valor de depósito inválido.");
        }
    }
}

// Classe ContaPoupanca que herda de Conta
@Data
public class ContaPoupanca extends Conta {
    private double taxaRendimento;
    
    public ContaPoupanca(String numero, String agencia, Cliente cliente, double saldoInicial, double taxaRendimento) {
        super(numero, agencia, cliente, saldoInicial);
        this.taxaRendimento = taxaRendimento;
    }
    
    @Override
    public void sacar(double valor) {
        if (valor <= this.saldo) {
            this.saldo -= valor;
            System.out.println("Saque realizado com sucesso!");
        } else {
            System.out.println("Saldo insuficiente.");
        }
    }
    
    @Override
    public void depositar(double valor) {
        if (valor > 0) {
            this.saldo += valor;
            System.out.println("Depósito realizado com sucesso!");
        } else {
            System.out.println("Valor de depósito inválido.");
        }
    }
    
    public void aplicarRendimento() {
        this.saldo += this.saldo * this.taxaRendimento;
        System.out.println("Rendimento aplicado!");
    }
}

// Classe Cliente
@Data
public class Cliente {
    @NonNull private String cpf;
    @NonNull private String nome;
    private String endereco;
    private String telefone;
    
    public Cliente(String cpf, String nome) {
        this.cpf = cpf;
        this.nome = nome;
    }
}

// Classe principal para testar o sistema
public class Banco {
    public static void main(String[] args) {
        // Criando clientes
        Cliente cliente1 = new Cliente("123.456.789-00", "João Silva");
        Cliente cliente2 = new Cliente("987.654.321-00", "Maria Souza");
        
        // Criando contas
        Conta corrente = new ContaCorrente("12345-6", "001", cliente1, 1000.0, 500.0);
        Conta poupanca = new ContaPoupanca("65432-1", "001", cliente2, 2000.0, 0.005);
        
        // Testando operações
        System.out.println("--- Operações na Conta Corrente ---");
        corrente.consultarSaldo();
        corrente.depositar(500.0);
        corrente.consultarSaldo();
        corrente.sacar(200.0);
        corrente.consultarSaldo();
        corrente.sacar(2000.0); // Testando saque além do saldo + cheque especial
        
        System.out.println("\n--- Operações na Conta Poupança ---");
        poupanca.consultarSaldo();
        poupanca.depositar(300.0);
        poupanca.consultarSaldo();
        ((ContaPoupanca)poupanca).aplicarRendimento(); // Método específico de poupança
        poupanca.consultarSaldo();
        
        System.out.println("\n--- Transferência entre contas ---");
        corrente.transferir(poupanca, 300.0);
        corrente.consultarSaldo();
        poupanca.consultarSaldo();
    }
}