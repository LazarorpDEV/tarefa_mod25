
package br.com.rpires.dao;

import br.com.rpires.dao.generic.GenericDAO;
import br.com.rpires.domain.Cliente;

public class ClienteDAO extends GenericDAO<Cliente, Long>
        implements IClienteDAO {

    public ClienteDAO() {
        super();
    }

    @Override
    public Class<Cliente> getTipoClasse() {
        return Cliente.class;
    }

    @Override
    public void atualiarDados(Cliente entity, Cliente entityCadastrado) {
        entityCadastrado.setNome(entity.getNome());
        entityCadastrado.setCpf(entity.getCpf());
        entityCadastrado.setTel(entity.getTel());
        entityCadastrado.setEnd(entity.getEnd());
        entityCadastrado.setNumero(entity.getNumero());
        entityCadastrado.setCidade(entity.getCidade());
        entityCadastrado.setEstado(entity.getEstado());
    }
}
