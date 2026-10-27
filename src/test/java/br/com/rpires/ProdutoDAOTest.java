
package br.com.rpires;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.rpires.dao.IProdutoDAO;
import br.com.rpires.dao.ProdutoDAO;
import br.com.rpires.domain.Produto;
import br.com.rpires.exceptions.TipoChaveNaoEncontradaException;

public class ProdutoDAOTest {

    private static final AtomicLong SEQUENCIA = new AtomicLong();

    private IProdutoDAO produtoDao;
    private Produto produto;

    @Before
    public void init() throws TipoChaveNaoEncontradaException {
        produtoDao = new ProdutoDAO();

        produto = new Produto();
        produto.setCodigo("PROD-" + SEQUENCIA.incrementAndGet());
        produto.setDescricao("Produto de teste");
        produto.setNome("Produto Teste");
        produto.setValor(BigDecimal.TEN);

        Assert.assertTrue(produtoDao.cadastrar(produto));
    }

    @Test
    public void pesquisar() {
        Produto encontrado = produtoDao.consultar(produto.getCodigo());

        Assert.assertNotNull(encontrado);
        Assert.assertEquals(produto.getCodigo(), encontrado.getCodigo());
    }

    @Test
    public void salvar() throws TipoChaveNaoEncontradaException {
        Produto novo = new Produto();
        novo.setCodigo("PROD-" + SEQUENCIA.incrementAndGet());
        novo.setNome("Novo Produto");
        novo.setValor(new BigDecimal("25.00"));

        Boolean retorno = produtoDao.cadastrar(novo);

        Assert.assertTrue(retorno);
        Assert.assertNotNull(produtoDao.consultar(novo.getCodigo()));
    }

    @Test
    public void excluir() {
        String codigo = produto.getCodigo();

        produtoDao.excluir(codigo);

        Assert.assertNull(produtoDao.consultar(codigo));
    }

    @Test
    public void alterarProduto()
            throws TipoChaveNaoEncontradaException {

        Produto alterado = new Produto();
        alterado.setCodigo(produto.getCodigo());
        alterado.setNome("Produto Atualizado");
        alterado.setDescricao("Nova descricao");
        alterado.setValor(new BigDecimal("99.90"));

        produtoDao.alterar(alterado);

        Produto encontrado = produtoDao.consultar(produto.getCodigo());

        Assert.assertNotNull(encontrado);
        Assert.assertEquals("Produto Atualizado", encontrado.getNome());
        Assert.assertEquals(
            0,
            new BigDecimal("99.90").compareTo(encontrado.getValor())
        );
    }

    @Test
    public void buscarTodos() {
        Collection<Produto> lista = produtoDao.buscarTodos();

        Assert.assertNotNull(lista);
        Assert.assertTrue(
            lista.stream().anyMatch(
                p -> produto.getCodigo().equals(p.getCodigo())
            )
        );
    }
}
