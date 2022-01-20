<template>
  <div>
    <el-form :inline="true">
      <el-form-item>
        <el-input
          v-model="searchForm.name"
          placeholder="名称"
          clearable=""
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getRoleList">搜索</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="dialogVisible = true">新增</el-button>
      </el-form-item>
      <el-form-item>
        <template>
          <el-popconfirm title="这是要批量删除吗？" @confirm="delHandle(null)">
            <el-button type="danger" slot="reference" :disabled="delBtlStatu"
              >批量删除</el-button
            >
          </el-popconfirm>
        </template>
      </el-form-item>
    </el-form>
    <el-table
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
      border
      stripe
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column prop="name" label="名称" width="120"> </el-table-column>
      <el-table-column
        prop="code"
        label="唯一编码"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="remark"
        label="描述"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column prop="statu" label="状态">
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.statu === 1" type="success"
            >正常</el-tag
          >
          <el-tag size="small" v-else-if="scope.row.statu === 0" type="danger"
            >禁用</el-tag
          >
        </template>
      </el-table-column>

      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="text" @click="permHandle(scope.row.id)"
            >分配权限</el-button
          >
          <el-divider direction="vertical"></el-divider>
          <el-button type="text" @click="editHandle(scope.row.id)"
            >编辑</el-button
          >

          <el-divider direction="vertical"></el-divider>
          <template>
            <el-popconfirm
              title="这是一段内容确定删除吗？"
              @confirm="delHandle(scope.row.id)"
            >
              <el-button type="text" slot="reference">删除</el-button>
            </el-popconfirm>
          </template>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      style="float: right; margin-top: 22px"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="current"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="size"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>

    <!-- 新增对话框 -->
    <el-dialog
      title="提示"
      :visible.sync="dialogVisible"
      width="600px"
      :before-close="handleClose"
    >
      <el-form
        :model="editForm"
        :rules="editFormRules"
        ref="editForm"
        label-width="100px"
      >
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="editForm.name"></el-input>
        </el-form-item>
        <el-form-item label="唯一编码" prop="code" label-width="100px">
          <el-input v-model="editForm.code" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="remark" label-width="100px">
          <el-input v-model="editForm.remark" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="状态" prop="statu" label-width="100px">
          <el-radio-group v-model="editForm.statu">
            <el-radio :label="0">禁用</el-radio>
            <el-radio :label="1">正常</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm('editForm')"
            >立即创建</el-button
          >
          <el-button @click="resetForm('editForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <!-- 分配权限对话框 -->
    <el-dialog title="分配权限" :visible.sync="permDialogVisible" width="600px">
      <el-form :model="permForm">
        <el-tree
          :data="permTreeData"
          :default-expand-all="true"
          show-checkbox
          ref="permTree"
          node-key="id"
          :check-strictly="true"
          :props="defaultProps"
        >
        </el-tree>
        <span slot="footer" class="dialog-footer">
          <el-button @click="permDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitPermFormHandle('permForm')"
            >确 定</el-button
          >
        </span>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Role",
  data() {
    return {
      multipleSelection: [],
      editFormRules: {
        name: [{ required: true, message: "请输入名称", trigger: "blur" }],
        code: [{ required: true, message: "请输入唯一编码", trigger: "blur" }],
        statu: [{ required: true, message: "请选择状态", trigger: "blur" }],
      },
      editForm: {},
      permForm: {},
      defaultProps: {
        children: "children",
        label: "name",
      },
      dialogVisible: false,
      permDialogVisible: false,
      total: 0,
      size: 10,
      current: 1,
      searchForm: {},
      delBtlStatu: true,
      permTreeData: [],
      tableData: [
        {
          name: "2016-05-03",
          code: "王小虎",
          remark: "上海市普陀区金沙江路 1518 弄",
          statu: 1,
        },
        {
          name: "2016-05-02",
          code: "王小虎",
          remark: "上海市普陀区金沙江路 1518 弄",
          statu: 1,
        },
        {
          name: "2016-05-04",
          code: "王小虎",
          remark: "上海市普陀区金沙江路 1518 弄",
          statu: 0,
        },
      ],
    };
  },
  created() {
    this.getRoleList();
    this.$axios.get("sys/menu/list").then((response) => {
      this.permTreeData = response.data.data;
      this.$axios.get("/sys/role/info/" + id).then((response) => {
        this.$refs.permTree.setCheckedKeys(response.data.data.menuIds);
        this.permForm = response.data.data
      });
    });
  },
  methods: {
    submitPermFormHandle(formName){
      var menuIds = this.$refs.permTree.getCheckedKeys()
      this.$axios.post('sys/role/perm/'+this.permForm.id,menuIds).then(response=>{
        this.$message({
          message: "操作成功",
          type: "success",
          onClose: () => {
            this.getMenuTree();
          },
        });
        this.permDialogVisible = false
      })
    },
    permHandle(id) {
      this.permDialogVisible = true;
    },
    delHandle() {
      var ids = [];
      if (id) {
        ids.push(id);
      } else {
        this.multipleSelection.forEach((row) => {
          ids.push(row.id);
        });
      }
      this.$axios.post("sys/role/delete/" + ids).then((response) => {
        this.$message({
          message: "删除成功",
          type: "success",
          onClose: () => {
            this.getMenuTree();
          },
        });
      });
    },
    getRoleList() {
      this.$axios
        .get("sys/role/list", {
          params: {
            name: this.searchForm.name,
            current: this.current,
            size: this.size,
          },
        })
        .then((response) => {
          this.tableData = response.data.data.records;
          this.size = response.data.data.size;
          this.current = response.data.data.current;
          this.total = response.data.data.total;
        });
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios
            .post(
              "/sys/role/" + (this.editForm.id ? "update" : "save"),
              this.editForm
            )
            .then((response) => {
              this.$message({
                message: "操作成功",
                type: "success",
                onClose: () => {
                  this.getMenuTree();
                },
              });
              this.dialogVisible = false;
            });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    editHandle(id) {
      this.$axios.get("/sys/role/info" + id).then((response) => {
        this.editForm = response.data.data;
        this.dialogVisible = true;
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.dialogVisible = false;
      this.editForm = {};
    },
    handleClose() {
      this.resetForm("editForm");
    },
    handleSizeChange(val) {
      this.size = val;
      this.getRoleList();
    },
    handleCurrentChange(val) {
      this.current = val;
      this.getRoleList();
    },
    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    handleSelectionChange(val) {
      this.delBtlStatu = val.length == 0;
      this.multipleSelection = val;
    },
  },
};
</script>

<style scoped>
</style>