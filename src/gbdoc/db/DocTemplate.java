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

@Entity(name = "doc_template")
@Table(name = "doc_template")
public class DocTemplate implements ISimplePoJo {
@Override
		public void setId(long id) {
			this.id = id;
		}	public static SimplePoJoDAO dao = new SimplePoJoDAO(DocTemplate.class);;

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
	@Column(name = "standard_section_id")
	public Long standard_section_id = null;

	@QueryStringField()
	@Column(name = "content")
	public String content = null;

// ====User code begins here====
// ====User code ends here====
}
