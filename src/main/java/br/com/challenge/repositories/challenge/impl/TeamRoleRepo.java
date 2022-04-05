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
import br.com.challenge.dto.TeamPresenter;
import br.com.challenge.dto.parser.TeamPresenterParser;
import br.com.challenge.entities.challenge.Role;
import br.com.challenge.entities.challenge.Team;
import br.com.challenge.entities.challenge.User;
import br.com.challenge.enums.DefaltRoleEnum;
import br.com.challenge.enums.SQLEnums;
import br.com.challenge.repositories.challenge.RoleManagerRepo;
import br.com.challenge.repositories.challenge.RoleRepo;
import br.com.challenge.repositories.challenge.UserRepo;

@Repository
public class TeamRoleRepo implements RoleRepo<TeamPresenter, Team>{

	@Autowired
	private EntityManager entityManager;
	
	@Autowired 
	private UserRepo userRepo;
	
	@Autowired
	private RoleManagerRepo roleManagRep;
	
	@Override
	public Optional<TeamPresenter> findEntity(Team team) {
		
		String val = "u, r";
		String table = "user_challenge u, role_challenge r"; 
		String cond = "u.id = r.idUser AND  r.idTeam =:id";
		String queryS = SQLEnums.SIMPLE_FIND_WITH_WHERE.getSql().replace("{val}", val).replace("{tab}", table).replace("{cond}", cond);
 		
		Query query = entityManager.createQuery(queryS);
		query.setParameter("id", team.getId());
		TeamPresenter resp = TeamPresenterParser.teamToPresenter(team);
		return parserTeamPresenter(query.getResultList(), resp);
	}
	
	private Optional<TeamPresenter>  parserTeamPresenter(List<Object[]> finded, TeamPresenter resp) {
		Map<String , List<User>> roles = new HashMap<>();
		for (Object[] objects : finded) {
			User user = (User) objects[0];
			Role role = (Role) objects[1];
			if(roles.containsKey(role.getRole())) {
				roles.get(role.getRole()).add(user);
			}else {
				List<User> users = new ArrayList<>();
				users.add(user);
				roles.put(role.getRole(), users);
			}
		}
		resp.setRole(roles);
		return Optional.of(resp);
	}

	@Override
	public Optional<TeamPresenter> insert(TeamPresenter dto) {
		if(dto.getTeamMemberIds() != null) dto.getTeamMemberIds().forEach(u -> inserDefaltUsers(u, dto.getId()));
		if(dto.getRole() == null) return findEntity(TeamPresenterParser.presenterToTeam(dto));
		for (Map.Entry<String, List<User>> entry : dto.getRole().entrySet()) {
			saveRole(entry.getKey(), entry.getValue(), dto.getId());
		}
		return findEntity(TeamPresenterParser.presenterToTeam(dto));
	}
	
	private void saveRole(String role, List<User> users, String id) {
		users.forEach(u -> insertUser(u));
		users.forEach(u -> insertRole(u, role, id));
	}
	
	private void inserDefaltUsers(String userId, String id) {
		Optional<User> user = userRepo.findById(userId);
		if(user.isPresent()) insertRole(user.get(), DefaltRoleEnum.DEVELOPER.getRole(), id);
	}
	
	private void insertRole(User user, String role, String id) {
		String cond = "r.idUser = '{user}' AND r.idTeam = '{team}' AND r.role = '{role}'"
				.replace("{user}", user.getId())
				.replace("{team}", id)
				.replace("{role}", role);
		String queryS  = SQLEnums.SIMPLE_FIND_WITH_WHERE.getSql()
			.replace("{val}", "r")
			.replace("{tab}", "role_challenge r")
			.replace("{cond}", cond);
		Query query = entityManager.createQuery(queryS);
		List<Role> roles = query.getResultList();
		if(roles == null || roles.isEmpty()) {
			roleManagRep.save(new Role(user.getId(), id, role));
		}
	}
	
	private void insertUser(User user) {
		userRepo.save(user);
	}

	@Override
	public DataPresenter findAllWithPag(Integer page, Integer size) {
		Long countResults = tableSize();
		Integer lastPage = (int) Math.ceil(countResults/size);
		
		List<Team> teamList = getTeamListPag(page, size);
		List<TeamPresenter> teamPresenterList = new ArrayList<TeamPresenter>();
		teamList.forEach(t -> teamPresenterList.add(this.findEntity(t).get()));
		
		MetaData meta = new MetaData(page, size, lastPage);
		DataPresenter data = new DataPresenter(teamList, meta);
		return data;
	}
	
	private Long tableSize() {
		String queryCountS = SQLEnums.SIMPLE_SELECT_COUNT.getSql().replace("{val}", "id").replace("{tab}", "team_challenge");
		Query queryCount = entityManager.createQuery(queryCountS);
		return (Long) queryCount.getSingleResult();
	}
	
	private List<Team> getTeamListPag(Integer page, Integer size){
		String queryS = "SELECT t FROM  team_challenge t";
		Query query = entityManager.createQuery(queryS);
		query.setFirstResult((page - 1) * size);
		query.setMaxResults(size);
		return query.getResultList();
	}

}
