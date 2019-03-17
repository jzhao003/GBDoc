package gbdoc.db;

//// This file is generated with class gubo.jdbc.mapping.TableClassGenerator.
import gubo.db.ISimplePoJo;
import gubo.db.SimplePoJoDAO;
import gubo.http.querystring.QueryStringField;
import gubo.jdbc.mapping.InsertStatementGenerator;
import gubo.jdbc.mapping.ResultSetMapper;
import gubo.jdbc.mapping.UpdateStatementGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import javax.sql.DataSource;

@Entity(name = "doc_field")
@Table(name = "doc_field")
public class DocField implements ISimplePoJo {
@Override
		public void setId(long id) {
			this.id = id;
		}	public static SimplePoJoDAO dao = new SimplePoJoDAO(DocField.class);;

	@Id()
	@GeneratedValue()
	@Column(name = "id")
	public Long id = null;

	@Column(name = "created_on")
	public Timestamp created_on = null;

	@Column(name = "updated_on")
	public Timestamp updated_on = null;

	@Column(name = "created_by")
	public Long created_by = null;

	@Column(name = "updated_by")
	public Long updated_by = null;

	@Column(name = "created_document_id")
	public Long created_document_id = null;

	@Column(name = "name")
	public String name = null;

	@Column(name = "field_value")
	public String field_value = null;

// ====User code begins here====
// ====User code ends here====
}
