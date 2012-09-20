package be.stijn.example.beans;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.PersistenceTest;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.TransactionMode;
import org.jboss.arquillian.persistence.Transactional;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import be.stijn.example.domain.Person;

/**
 * 
 * This test is using {@link PersistenceTest} annotation which is marker
 * annotation for triggering persistence extension. All tests within marked test
 * class are wrapped in transaction.
 * 
 */
@RunWith(Arquillian.class)
@PersistenceTest
@Transactional(TransactionMode.ROLLBACK)
public class PersonBeanIntegrationTest {

	@EJB
	private PersonBean personBean;

	@Deployment
	public static JavaArchive createTestArchive() {
		return ShrinkWrap.create(JavaArchive.class, "test.jar") // Create jar
				.addPackage(Person.class.getPackage()) // Add classes
				.addPackage(PersonBean.class.getPackage()) // Add more classes
				// FEST Assert is not part of Arquillian JUnit
				.addPackages(true, "org.fest")
				// .addClasses(Person.class, PersonBean.class) is an alternative
				// for addPackage
				.addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml")) // b
				.addAsResource("META-INF/persistence.xml");
	}

	@Test
	@UsingDataSet("dataset/PersonBeanIntegrationTest.xml")
	public void findPersonbyId() {
		Person result = personBean.findPerson(1L);

		assertEquals("foo", result.getFirstName());
		assertEquals("bar", result.getLastName());
	}

	@Test
	@UsingDataSet("dataset/PersonBeanIntegrationTest.xml")
	public void findAll() {
		List<Person> result = personBean.findAll();

		assertEquals(2, result.size());
		assertEquals("foo", result.get(0).getFirstName());
		assertEquals("bar", result.get(0).getLastName());
		assertEquals("duke", result.get(1).getFirstName());
		assertEquals("java", result.get(1).getLastName());
	}

	@Test
	@UsingDataSet("dataset/PersonBeanIntegrationTest.xml")
	@ShouldMatchDataSet("dataset/PersonBeanIntegrationTest_save.xml")
	@Transactional(TransactionMode.COMMIT)
	public void save() {
		Person p = new Person();
		p.setFirstName("firstname");
		p.setLastName("lastname");

		personBean.save(p);

	}

}
