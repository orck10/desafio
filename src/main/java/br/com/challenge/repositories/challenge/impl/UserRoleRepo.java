package br.com.challenge.repositories.challenge.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.challenge.dto.DataPresenter;
import br.com.challenge.dto.MetaData;
import br.com.challenge.dto.UserPresenter;
import br.com.challenge.dto.parser.UserPresenterParser;
import br.com.challenge.entities.challenge.Role;
import br.com.challenge.entities.challenge.RoleEntity;
import br.com.challenge.entities.challenge.Team;
import br.com.challenge.entities.challenge.User;
import br.com.challenge.enums.SQLEnums;
import br.com.challenge.repositories.challenge.RoleManagerRepo;
import br.com.challenge.repositories.challenge.RoleRepo;
import br.com.challenge.repositories.challenge.TeamRepo;

@Repository
public class UserRoleRepo implements RoleRepo<UserPresenter, User>{
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private TeamRepo teamRepo;
	
	@Autowired
	private RoleManagerRepo roleManagRep;

	@Override
	public Optional<UserPresenter> findEntity(User user) {
		String val = "t, r";
		String table = "team_challenge t, role_challenge r"; 
		String cond = "t.id = r.idTeam AND  r.idUser =:id";
		String queryS = SQLEnums.SIMPLE_FIND_WITH_WHERE.getSql().replace("{val}", val).replace("{tab}", table).replace("{cond}", cond);
 		
		Query query = entityManager.createQuery(queryS);
		query.setParameter("id", user.getId());
		UserPresenter resp = UserPresenterParser.userToPresenter(user);
		return parserTeamPresenter(query.getResultList(), resp);
	}
	
	private Optional<UserPresenter>  parserTeamPresenter(List<Object[]> finded, UserPresenter resp) {
		Map<String , List<Team>> roles = new HashMap<>();
		for (Object[] objects : finded) {
			Team team = (Team) objects[0];
			Role role = (Role) objects[1];
			if(roles.containsKey(role.getRole())) {
				roles.get(role.getRole()).add(team);
			}else {
				List<Team> teams = new ArrayList<>();
				teams.add(team);
				roles.put(role.getRole(), teams);
			}
		}
		resp.setRole(roles);
		return Optional.of(resp);
	}

	@Override
	public Optional<UserPresenter> insert(UserPresenter dto) {
		if(dto.getRole() == null) return Optional.of(dto);
		for (Map.Entry<String, List<Team>> entry : dto.getRole().entrySet()) {
			if(roleVerify(entry.getKey())) saveRole(entry.getKey(), entry.getValue(), dto.getId());
		}
		return findEntity(UserPresenterParser.presenterToUser(dto));
	}
	
	private void saveRole(String role, List<Team> teams, String id) {
		teams.forEach(t -> insertTeam(t));
		teams.forEach(t -> insertRole(t, role, id));
	}
	
	private void insertRole(Team team, String role, String id) {
		
		String cond = "r.idUser = '{user}' AND r.idTeam = '{team}' AND r.role = '{role}'"
				.replace("{user}", id)
				.replace("{team}", team.getId())
				.replace("{role}", role);
		String queryS  = SQLEnums.SIMPLE_FIND_WITH_WHERE.getSql()
			.replace("{val}", "r")
			.replace("{tab}", "role_challenge r")
			.replace("{cond}", cond);
		Query query = entityManager.createQuery(queryS);
		List<Role> roles = query.getResultList();
		if(roles == null || roles.isEmpty()) {
			roleManagRep.save(new Role(id, team.getId(), role));
		}
		
	}
	
	private boolean roleVerify(String role) {
		String cond = " r.roleName = '{role}'".replace("{role}", role); 
		String queryS = SQLEnums.SIMPLE_FIND_WITH_WHERE.getSql()
				.replace("{val}", "r")
				.replace("{tab}", "role_entity_challenge r")
				.replace("{cond}", cond);
		
		Query query = entityManager.createQuery(queryS);
		List<RoleEntity> roles = query.getResultList();
		return role != null && !roles.isEmpty();
	}
	
	private void insertTeam(Team team) {
		teamRepo.save(team);
	}

	@Override
	public DataPresenter findAllWithPag(Integer page, Integer size) {
		Long countResults = tableSize();
		Integer lastPage = (int) Math.ceil(countResults/size);
		
		List<User> userList = getUserListPag(page, size);
		List<UserPresenter> userPresenterList = new ArrayList<UserPresenter>();
		userList.forEach(u -> userPresenterList.add(this.findEntity(u).get()));
		
		MetaData meta = new MetaData(page, size, lastPage);
		DataPresenter data = new DataPresenter(userPresenterList, meta);
		return data;
	}
	
	private Long tableSize() {
		String queryCountS = SQLEnums.SIMPLE_SELECT_COUNT.getSql().replace("{val}", "id").replace("{tab}", "user_challenge");
		Query queryCount = entityManager.createQuery(queryCountS);
		return (Long) queryCount.getSingleResult();
	}
	
	private List<User> getUserListPag(Integer page, Integer size){
		String queryS = "SELECT u FROM  user_challenge u";
		Query query = entityManager.createQuery(queryS);
		query.setFirstResult((page - 1) * size);
		query.setMaxResults(size);
		return query.getResultList();
	}
	
}
