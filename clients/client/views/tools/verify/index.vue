<template>
  <div>
    <div class="tile is-ancestor">
      <div class="tile is-parent is-12">
        <div class="tile is-child box">
          <div class="notification is-blue">
            <p class="subtitle">Create New Job</p>
          </div>
          <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
            <el-form-item label="Email:" prop="email">
              <el-col :span="10">
                <el-input placeholder="Please enter your veritas email address" v-model="ruleForm.email" @blur="removeSuffix">
                  <template slot="append" type="primary">@veritas.com</template>
                </el-input>
              </el-col>
            </el-form-item>
            <el-form-item label="Hostnames:" prop="hostname">
              <el-select @change="changeHostname(verifyTestbeds)" v-model="ruleForm.hostname" filterable multiple remote style="width:95%" :remote-method="remoteMethod" :loading="loading">
                <el-option v-for="item in verifyTestbeds" :label="item.hostname" :value="item.hostname" :key="item.hostname"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-table :data="testBeds" stripe border align="center" style="width:95%">
                <el-table-column prop="hostname" label="Testbed" width="160">
                </el-table-column>
                <el-table-column prop="pool" label="Owner" width="150">
                </el-table-column>
                <el-table-column prop="site" label="Site" width="120">
                </el-table-column>
                <el-table-column prop="model" label="Model" width="80">
                </el-table-column>
                <el-table-column prop="hosttype" label="Type" width="160">
                </el-table-column>
                <el-table-column prop="scenario" label="Scenario">
                  <template scope="scope">
                    {{scope.row.value}}
                    <el-select v-model="scope.row.value" style="width:95%" clearable placeholder="please select predefined scenario">
                      <el-option  v-for="item in options" :key="item.label" :label="item.label" :value="item.value">
                      </el-option>
                    </el-select>
                  </template>
                </el-table-column>
              </el-table>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm('ruleForm')">Submit</el-button>
              <el-button @click="resetForm('ruleForm')">Reset</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>

    <div class="tile is-ancestor">
      <div class="tile is-parent is-12">
        <div class="tile is-child box">
          <div class="notification is-blue">
            <p class="subtitle">Job History</p>
          </div>
          <el-tooltip content="Refresh Job History" placement="top"><el-button type="primary" class="fa fa-refresh" @click="getVerifyStatsByPage(1)"></el-button></el-tooltip>
          <el-table :data="filterVerifyStatsByPage" stripe border style="width: 95%">
            <el-table-column type="expand" width="50">
              <template scope="scope">
                <el-form  label-position="left" inline class="demo-table-expand">
                  <el-form-item label="Deploy:">
                    <span>{{ scope.row.deployResult }}</span>
                  </el-form-item>
                  <el-form-item label="Precheck:">
                    <span>{{ scope.row.precheckResult }}</span>
                  </el-form-item>
                  <el-form-item label="Sanity:">
                    <span><a :href="scope.row.sanityResult">{{ scope.row.sanityResult }}</a></span>
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column prop="hostname" label="Appliance" width="150">
            </el-table-column>
            <el-table-column prop="pool" label="Owner" width="150">
            </el-table-column>
            <el-table-column prop="status" label="Status" width="100" :filters="[{ text: 'pending', value: 'pending' }, { text: 'running', value: 'running' }, { text: 'passed', value: 'passed' }, { text: 'failed', value: 'failed' }, { text: 'timeout', value: 'timeout' }, { text: 'cancel', value: 'cancel' }]"
              :filter-method="filterStatus">
              <template scope="scope">
                <el-tag class="animated zoomIn" :type="getColor(scope.row.status)" close-transition>{{scope.row.status}}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="email" label="Email" width="220">
            </el-table-column>
            <el-table-column prop="process" label="Progress">
              <template scope="scope">
                <div class="process">
                  <div class="columns">
                    <div class="column is-offset-2">
                      <el-steps :active="scope.row.active" :process-status="scope.row.processStatus" finish-status="success">
                        <el-step title="Deploy"  icon="setting" :description="scope.row.deployDescription"></el-step>
                        <el-step title="Precheck"  icon="setting" :description="scope.row.precheckDescription"></el-step>
                        <el-step title="Sanity"  icon="setting" :description="scope.row.sanityDescription"></el-step>
                      </el-steps>
                    </div>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="CreateTime" width="170">
            </el-table-column>
            <el-table-column label="Operation" width="110">
              <template scope="scope">
                <el-button type="text" @click="handleCancel(scope.$index, scope.row)">Cancel</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="columns">
            <div class="column is-offset-5">
              <el-pagination layout="prev, pager, next" :total="totalVerifyJob * 10" @current-change="getVerifyStatsByPage" style="border-radius: 0px"
                :page-size="10"></el-pagination>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import _ from 'lodash'
import moment from 'moment'

export default {
  data () {
    return {
      options: [{
        value: 'Appliance_Role_Config',
        label: 'Appliance_Role_Config'
      }, {
        value: '5230_Master_Clish',
        label: '5230_Master_Clish'
      }, {
        value: '5230_Master_Clish_Sanity',
        label: '5230_Master_Clish_Sanity'
      }, {
        value: '5330_Media_Clish_Sanity',
        label: '5330_Media_Clish_Sanity'
      }, {
        value: '5220_Media_Clish_Sanity',
        label: '5220_Media_Clish_Sanity'
      }],
      testBeds: [],
      loading: false,
      deployDescription: '',
      sanityDescription: '',
      precheckDescription: '',
      processStatus: 'wait',
      active: 0,
      deployReason: '',
      precheckReason: '',
      sanityReason: '',
      deployStatus: '',
      preCheckStatus: '',
      sanityStatus: '',
      jobstatus: '',
      tableData: [],
      hostname: '',
      ruleForm: {
        email: '',
        hostname: ''
      },
      rules: {
        email: [
          { required: true, message: 'Please enter veritas email address', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    ...mapGetters({
      verifyStatsByPage: 'verifyStatsByPage',
      verifyTestbeds: 'verifyTestbeds',
      totalVerifyJob: 'totalVerifyJob'
    }),
    filterVerifyStatsByPage () {
      var testbedsRecord = _.reduce(this.verifyStatsByPage.content, (result, c) => {
        for (var state of c.status) {
          this.jobstatus = c.state
          let s = state.state
          let n = state.name
          if (n === 'deploy') {
            this.deployReason = state.reason
            if (s === 'cancel') {
              this.active = 0
              this.processStatus = 'wait'
              this.deployStatus = s
              this.preCheckStatus = s
              this.sanityStatus = s
            }
            if (s === 'running' || s === 'pending') {
              this.active = 0
              this.processStatus = 'process'
              this.deployStatus = s
            }
            if (s === 'failed' || s === 'timeout') {
              this.active = 0
              this.processStatus = 'error'
              this.deployStatus = s
              this.preCheckStatus = ''
              this.sanityStatus = ''
            }
            if (s === 'done') {
              this.deployStatus = 'passed'
            }
          } else if (n === 'preCheck') {
            this.precheckReason = state.reason
            if (s === 'cancel') {
              this.active = 1
              this.processStatus = 'wait'
              this.deployStatus = s
              this.preCheckStatus = s
              this.sanityStatus = s
            }
            if (s === 'running') {
              this.active = 1
              this.processStatus = 'process'
              this.preCheckStatus = s
            }
            if (s === 'failed' || s === 'timeout') {
              this.active = 1
              this.processStatus = 'error'
              this.preCheckStatus = s
              this.sanityStatus = ''
            }
            if (s === 'pending') {
              this.preCheckStatus = s
            }
            if (s === 'done') {
              this.preCheckStatus = 'passed'
            }
          } else if (n === 'sanity') {
            this.sanityReason = state.reason
            if (s === 'cancel') {
              this.active = 2
              this.processStatus = 'wait'
              this.deployStatus = s
              this.preCheckStatus = s
              this.sanityStatus = s
            }
            if (s === 'running') {
              this.active = 2
              this.processStatus = 'process'
              this.sanityStatus = s
            }
            if (s === 'failed' || s === 'timeout') {
              this.active = 2
              this.processStatus = 'error'
              this.sanityStatus = s
            }
            if (s === 'done') {
              this.active = 2
              this.processStatus = 'success'
              this.sanityStatus = 'passed'
            }
          }
        }
        let deployState = _.find(c.status, v => v.name === 'deploy').state
        let preCheckState = _.find(c.status, v => v.name === 'preCheck').state
        let sanityState = _.find(c.status, v => v.name === 'sanity').state
        if (deployState === 'done' && preCheckState === 'pending' && sanityState === 'pending') {
          this.active = 0
          this.processStatus = 'success'
          this.deployStatus = deployState
          this.preCheckStatus = preCheckState
          this.sanityStatus = sanityState
        }
        if (deployState === 'done' && preCheckState === 'done' && sanityState === 'pending') {
          this.active = 1
          this.processStatus = 'success'
          this.deployStatus = deployState
          this.preCheckStatus = preCheckState
          this.sanityStatus = sanityState
        }
        if (deployState === 'failed' || deployState === 'timeout') {
          this.active = 0
          this.processStatus = 'error'
          this.deployStatus = deployState
          this.preCheckStatus = ''
          this.sanityStatus = ''
        }
        if (this.jobstatus === 'cancel') {
          this.active = 0
          this.processStatus = 'wait'
          this.deployStatus = this.jobstatus
          this.preCheckStatus = this.jobstatus
          this.sanityStatus = this.jobstatus
        }
        result.push({
          'hostname': c.hostname,
          'email': c.email,
          'createTime': moment(c.createTime).format('YYYY-MM-DD HH:mm:ss'),
          'status': this.jobstatus,
          'deployDescription': this.deployStatus,
          'sanityDescription': this.sanityStatus,
          'precheckDescription': this.preCheckStatus,
          'deployResult': this.deployReason,
          'sanityResult': this.sanityReason,
          'precheckResult': this.precheckReason,
          'active': this.active,
          'processStatus': this.processStatus
        })
        return result
      }, [])
      return testbedsRecord
    }
  },

  methods: {
    remoteMethod: _.debounce(function (query) {
      this.loading = true
      setTimeout(() => {
        this.loading = false
        this.$store.dispatch('getTestbedsInfo', {'hostname': query})
      }, 200)
    }, 350),
    changeHostname (value) {
      if (value) {
        let matchTestBeds = _.map(this.ruleForm.hostname, v => _.find(value, item => item.hostname === v))
        _.map(_.uniq(_.compact(matchTestBeds)), v => this.testBeds.push(v))
        this.testBeds = _.uniq(_.compact(this.testBeds))
        if (this.ruleForm.hostname.length < this.testBeds.length) {
          let deleteTestBedHostnames = _.difference(_.map(this.testBeds, item => item.hostname), this.ruleForm.hostname)
          let deleteTestBeds = _.map(deleteTestBedHostnames, v => _.find(this.testBeds, testBed => testBed.hostname === v))
          _.map(deleteTestBeds, v => this.testBeds.pop(v))
        }
      }
    },
    removeSuffix () {
      if (this.ruleForm.email) {
        this.ruleForm.email = this.ruleForm.email.split('@')[0]
      }
    },
    getColor (status) {
      if (status === 'pending') return 'light'
      if (status === 'running') return 'primary'
      if (status === 'failed') return 'danger'
      if (status === 'timeout' || status === 'cancel') return 'warning'
      if (status === 'passed') return 'success'
    },
    postVerifyJob (index, vm) {
      if (this.testBeds[index].hostname && this.testBeds[index].model && this.testBeds[index].hosttype && this.testBeds[index].site) {
        this.$store.dispatch('postVerifyJob',
          {
            'hostname': this.testBeds[index].hostname,
            'model': this.testBeds[index].model,
            'type': this.testBeds[index].hosttype,
            'email': this.ruleForm.email + '@veritas.com',
            'site': this.testBeds[index].site,
            'username': this.ruleForm.email,
            'vm': vm
          }
        )
      }
    },
    getVerifyStatsByPage (page) {
      this.$store.dispatch('getVerifyStatsByPage', { 'page': page - 1 })
    },
    filterStatus (value, row) {
      return row.status === value
    },
    handleCancel (index, row) {
      let vm = this
      this.$store.dispatch('cancelCheckAppliance', {'hostname': row.hostname, 'vm': vm})
    },
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          var testBedNum = this.testBeds.length
          let vm = this
          for (var index = 0; index < testBedNum; index++) {
            this.postVerifyJob(index, vm)
          }
          this.$refs[formName].resetFields()
        } else {
          return false
        }
      })
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    },
    next () {
      if (this.active++ > 2) this.active = 0
    }
  },
  created () {
    this.$store.dispatch('getVerifyStatsByPage', { 'page': 0 })
    this.$store.dispatch('getTestbedsInfo', {'hostname': ''})
  }
}
</script>

<style>
  .fa-refresh {
    margin-bottom: 10px;
  }
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
  .process {
    margin-top: 10px;
    margin-bottom: 10px;
    transform:scale(0.9)
  }
  .el-row {
    margin-top: 5px;
    margin-right: 0;
  }
</style>
