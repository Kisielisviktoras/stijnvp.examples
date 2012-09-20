package be.stijn.example.jsf;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import be.stijn.example.beans.PersonBean;
import be.stijn.example.domain.Person;

@ManagedBean(name = "example")
@RequestScoped
public class Example {

	@EJB
	private PersonBean personBean;

	private Person person = new Person();

	public List<Person> getPersons() {
		return personBean.findAll();
	}

	public Person getPerson() {
		return person;
	}

	public void persistPerson() {
		personBean.save(person);
	}

}
