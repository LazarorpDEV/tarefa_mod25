
package br.com.rpires;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.rpires.dao.ClienteDAO;
import br.com.rpires.dao.IClienteDAO;
import br.com.rpires.domain.Cliente;
import br.com.rpires.exceptions.TipoChaveNaoEncontradaException;

public class ClienteDAOTest {

    private static final AtomicLong SEQUENCIA =
            new AtomicLong(10000000000L);

    private IClienteDAO clienteDao;
    private Cliente cliente;

    @Before
    public void init() throws TipoChaveNaoEncontradaException {
        clienteDao = new ClienteDAO();

        cliente = new Cliente();
        cliente.setCpf(SEQUENCIA.incrementAndGet());
        cliente.setNome("Cliente Teste");
        cliente.setCidade("Sao Paulo");
        cliente.setEnd("Rua Teste");
        cliente.setEstado("SP");
        cliente.setNumero(10);
        cliente.setTel(11999999999L);

        Assert.assertTrue(clienteDao.cadastrar(cliente));
    }

    @Test
    public void pesquisarCliente() {
        Cliente encontrado = clienteDao.consultar(cliente.getCpf());

        Assert.assertNotNull(encontrado);
        Assert.assertEquals(cliente.getCpf(), encontrado.getCpf());
    }

    @Test
    public void salvarCliente()
            throws TipoChaveNaoEncontradaException {

        Cliente novo = new Cliente();
        novo.setCpf(SEQUENCIA.incrementAndGet());
        novo.setNome("Novo Cliente");

        Boolean retorno = clienteDao.cadastrar(novo);

        Assert.assertTrue(retorno);
        Assert.assertNotNull(clienteDao.consultar(novo.getCpf()));
    }

    @Test
    public void excluirCliente() {
        Long cpf = cliente.getCpf();

        clienteDao.excluir(cpf);

        Assert.assertNull(clienteDao.consultar(cpf));
    }

    @Test
    public void alterarCliente()
            throws TipoChaveNaoEncontradaException {

        Cliente alterado = new Cliente();
        alterado.setCpf(cliente.getCpf());
        alterado.setNome("Cliente Atualizado");
        alterado.setCidade("Teixeira de Freitas");

        clienteDao.alterar(alterado);

        Cliente encontrado = clienteDao.consultar(cliente.getCpf());

        Assert.assertNotNull(encontrado);
        Assert.assertEquals("Cliente Atualizado", encontrado.getNome());
        Assert.assertEquals("Teixeira de Freitas", encontrado.getCidade());
    }

    @Test
    public void buscarTodos() {
        Collection<Cliente> lista = clienteDao.buscarTodos();

        Assert.assertNotNull(lista);
        Assert.assertTrue(
            lista.stream().anyMatch(
                c -> cliente.getCpf().equals(c.getCpf())
            )
        );
    }
}
