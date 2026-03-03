package com.project.cinema.repositories;

import com.project.cinema.dtos.relatorio.RelatorioItemDTO.RelatorioFiltroDTO;
import com.project.cinema.dtos.relatorio.RelatorioItemDTO;
import com.project.cinema.models.Ingresso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface IngressoRepository extends JpaRepository<Ingresso, UUID> {
    List<Ingresso> findByUsuarioId(UUID usuarioId);

    List<Ingresso> findBySessaoId(UUID sessaoId);

    @Query("""
            select new com.project.cinema.dtos.relatorio.RelatorioItemDTO(
                f.nome,
                count(i.id),
                coalesce(sum(coalesce(i.valorI, 0.0) - coalesce(i.valorDesconto, 0.0)), 0.0),
                count(distinct s.id)
            )
            from Ingresso i
            join i.sessao s
            join s.filme f
            where (:#{#filtro.dataInicio} is null or i.dataCompra >= :#{#filtro.dataInicio})
              and (:#{#filtro.dataFim} is null or i.dataCompra <= :#{#filtro.dataFim})
              and (:#{#filtro.genero} is null or :#{#filtro.genero} = ''
                   or lower(f.genero) = lower(:#{#filtro.genero}))
            group by f.id, f.nome
            """)
    List<RelatorioItemDTO> buscarResumoPorFilme(@Param("filtro") RelatorioFiltroDTO filtro);

    @Query("""
            select coalesce(sum(coalesce(sa.numAssentos, 0)), 0)
            from Sessao s
            join s.filme f
            join s.sala sa
            where lower(f.nome) = lower(:nomeFilme)
              and (:#{#filtro.genero} is null or :#{#filtro.genero} = ''
                   or lower(f.genero) = lower(:#{#filtro.genero}))
              and exists (
                  select 1
                  from Ingresso i
                  where i.sessao = s
                    and (:#{#filtro.dataInicio} is null or i.dataCompra >= :#{#filtro.dataInicio})
                    and (:#{#filtro.dataFim} is null or i.dataCompra <= :#{#filtro.dataFim})
              )
            """)
    Long buscarCapacidadeTotalPorFilme(@Param("filtro") RelatorioFiltroDTO filtro, @Param("nomeFilme") String nomeFilme);

    @Query("""
            select i.tipoIngresso, count(i.id)
            from Ingresso i
            join i.sessao s
            join s.filme f
            where lower(f.nome) = lower(:nomeFilme)
              and (:#{#filtro.dataInicio} is null or i.dataCompra >= :#{#filtro.dataInicio})
              and (:#{#filtro.dataFim} is null or i.dataCompra <= :#{#filtro.dataFim})
              and (:#{#filtro.genero} is null or :#{#filtro.genero} = ''
                   or lower(f.genero) = lower(:#{#filtro.genero}))
            group by i.tipoIngresso
            order by count(i.id) desc, i.tipoIngresso asc
            """)
    List<Object[]> buscarContagemPorTipoIngresso(@Param("filtro") RelatorioFiltroDTO filtro, @Param("nomeFilme") String nomeFilme);

    @Query("""
            select count(distinct i.usuario.id)
            from Ingresso i
            join i.sessao s
            join s.filme f
            where lower(f.nome) = lower(:nomeFilme)
              and (:#{#filtro.dataInicio} is null or i.dataCompra >= :#{#filtro.dataInicio})
              and (:#{#filtro.dataFim} is null or i.dataCompra <= :#{#filtro.dataFim})
              and (:#{#filtro.genero} is null or :#{#filtro.genero} = ''
                   or lower(f.genero) = lower(:#{#filtro.genero}))
            """)
    Long buscarTotalClientesUnicosPorFilme(@Param("filtro") RelatorioFiltroDTO filtro, @Param("nomeFilme") String nomeFilme);
}
