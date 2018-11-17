package gbdoc.db;

//// This file is generated with class gubo.jdbc.mapping.TableClassGenerator.
import gubo.db.ISimplePoJo;
import gubo.db.SimplePoJoDAO;
import gubo.http.querystring.QueryStringField;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "standard")
@Table(name = "standard")
public class Standard implements ISimplePoJo {
@Override
		public void setId(long id) {
			this.id = id;
		}	public static SimplePoJoDAO dao = new SimplePoJoDAO(Standard.class);;

	@Id()
	@GeneratedValue()
	@Column(name = "id")
	public Long id = null;

	@Column(name = "created_on")
	public Timestamp created_on = new Timestamp(System.currentTimeMillis());

	@Column(name = "updated_on")
	public Timestamp updated_on = new Timestamp(System.currentTimeMillis());

	@Column(name = "created_by")
	public Long created_by = null;

	@Column(name = "updated_by")
	public Long updated_by = null;

	@QueryStringField()
	@Column(name = "title")
	public String title = null;

	@QueryStringField(required=false)
	@Column(name = "template_location")
	public String template_location = null;

// ====User code begins here====
// ====User code ends here====
}
