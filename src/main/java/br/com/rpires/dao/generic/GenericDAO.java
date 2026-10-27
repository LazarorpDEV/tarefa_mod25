
package br.com.rpires.dao.generic;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import anotacao.TipoChave;
import br.com.rpires.dao.Persistente;
import br.com.rpires.exceptions.TipoChaveNaoEncontradaException;

public abstract class GenericDAO<T extends Persistente, E extends Serializable>
        implements IGenericDAO<T, E> {

    private final SingletonMap singletonMap;

    public abstract Class<T> getTipoClasse();

    // Mantido com o nome utilizado no projeto do professor
    public abstract void atualiarDados(T entity, T entityCadastrado);

    public GenericDAO() {
        this.singletonMap = SingletonMap.getInstance();

        Class<T> tipoClasse = getTipoClasse();

        if (tipoClasse == null) {
            throw new IllegalStateException(
                "getTipoClasse() nao pode retornar null."
            );
        }

        // Inicializa o mapa correspondente a entidade
        getMapa();
    }

    @SuppressWarnings("unchecked")
    public E getChave(T entity)
            throws TipoChaveNaoEncontradaException {

        Field[] fields = entity.getClass().getDeclaredFields();

        for (Field field : fields) {

            if (field.isAnnotationPresent(TipoChave.class)) {

                TipoChave tipoChave =
                    field.getAnnotation(TipoChave.class);

                String nomeMetodo = tipoChave.value();

                try {
                    Method method =
                        entity.getClass().getMethod(nomeMetodo);

                    return (E) method.invoke(entity);

                } catch (NoSuchMethodException |
                            IllegalAccessException |
                            InvocationTargetException e) {

                    throw new TipoChaveNaoEncontradaException(
                        "Chave principal do objeto "
                        + entity.getClass().getName()
                        + " nao encontrada", e
                    );
                }
            }
        }

        throw new TipoChaveNaoEncontradaException(
            "Anotacao @TipoChave nao encontrada na classe "
            + entity.getClass().getName()
        );
    }

    @SuppressWarnings("unchecked")
    private Map<E, T> getMapa() {

        Class<T> tipoClasse = getTipoClasse();

        if (tipoClasse == null) {
            throw new IllegalStateException(
                "getTipoClasse() nao pode retornar null."
            );
        }

        Map<E, T> mapaInterno =
            (Map<E, T>) singletonMap.getMap().get(tipoClasse);

        if (mapaInterno == null) {
            mapaInterno = new HashMap<>();

            singletonMap.getMap().put(
                tipoClasse, mapaInterno
            );
        }

        return mapaInterno;
    }

    @Override
    public Boolean cadastrar(T entity)
            throws TipoChaveNaoEncontradaException {

        Map<E, T> mapaInterno = getMapa();

        E chave = getChave(entity);

        if (mapaInterno.containsKey(chave)) {
            return false;
        }

        mapaInterno.put(chave, entity);

        return true;
    }

    @Override
    public void excluir(E valor) {

        Map<E, T> mapaInterno = getMapa();

        mapaInterno.remove(valor);
    }

    @Override
    public void alterar(T entity)
            throws TipoChaveNaoEncontradaException {

        Map<E, T> mapaInterno = getMapa();

        E chave = getChave(entity);

        T objetoCadastrado = mapaInterno.get(chave);

        if (objetoCadastrado != null) {
            atualiarDados(entity, objetoCadastrado);
        }
    }

    @Override
    public T consultar(E valor) {

        Map<E, T> mapaInterno = getMapa();

        return mapaInterno.get(valor);
    }

    @Override
    public Collection<T> buscarTodos() {

        Map<E, T> mapaInterno = getMapa();

        return mapaInterno.values();
    }
}