package gbdoc.db;

//// This file is generated with class gubo.jdbc.mapping.TableClassGenerator.
import gubo.db.ISimplePoJo;
import gubo.db.SimplePoJoDAO;
import gubo.http.querystring.QueryStringField;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "standard_section")
@Table(name = "standard_section")
public class StandardSection implements ISimplePoJo {
@Override
		public void setId(long id) {
			this.id = id;
		}	public static SimplePoJoDAO dao = new SimplePoJoDAO(StandardSection.class);;

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
	@Column(name = "section_number")
	public String section_number = null;

	@QueryStringField(required=false)
	@Column(name = "section_content")
	public String section_content = null;

	@QueryStringField()
	@Column(name = "standard_id")
	public Long standard_id = null;

	// no use, to be deleted?
	@Column(name = "doc_title")
	public String doc_title = null;

	// no use, to be deleted?
	@QueryStringField(required=false)
	@Column(name = "location")
	public Integer location = null;

// ====User code begins here====
// ====User code ends here====
}
