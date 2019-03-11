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
	
	@QueryStringField(required=false)
	@Column(name = "standard_id")
	public Long standard_id = null;
	
	// 模板文件路径
	@QueryStringField(required=false)
	@Column(name = "file_path")
	public String file_path = null;

// ====User code begins here====
// ====User code ends here====
}
