<template>
  <div class="tile is-ancestor">
    <div class="tile is-parent is-12">
      <div class="tile is-child box">
        <el-button type="text" @click="openModal" >Create</el-button>
        <card-modal :visible="dialogFormVisible" v-on:ok="submit" v-on:cancel="cancel" @close="closeModal" transition="zoom">
          <label class="label">Failure Type</label>
          <p class="control">
            <span class="select">
              <select v-model= "typeSelected" style="width:250px">
                <option v-for="failureType in failureTypes" :value="failureType">{{failureType}}</option>
              </select>
            </span>
          </p>
          <label class="label">Pipeline</label>
          <p class="control">
            <span class="select">
              <select v-model= "pipelineSelected" style="width:250px">
                <option v-for="pipeline in pipelines" :value="pipeline.name">{{pipeline.name}}</option>
              </select>
            </span>
          </p>
          <label class="label">Build</label>
          <p class="control">
            <span class="select">
              <select v-model= "buildSelected" style="width:250px">
                <option v-for="failedDeploymentStat in failedDeploymentStats" :value="failedDeploymentStat.triggerId">{{failedDeploymentStat.triggerId}}</option>
              </select>
            </span>
          </p>
          <label class="label">Appliance</label>
          <p class="control">
            <span class="select">
              <select v-model= "applianceSelected" style="width:250px">
                <option v-for="appliance in appliances" :value="appliance">{{appliance}}</option>
              </select>
            </span>
          </p>
          <label class="label">JIRA</label>
          <p class="control">
            <textarea class="text" style="width:250px" placeholder="Please input JITA ticket"></textarea>
          </p>
          <label class="label">RootCause</label>
          <p class="control">
            <textarea class="textarea" placeholder="Please input the rootcause of failure"></textarea>
          </p>
        </card-modal>
        <el-table :data="tableData3" border style="width: 100%" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55">
          </el-table-column>
          <el-table-column prop="pipeline" label="Pipeline" width="200"
            :filters="[{ text: 'nba_pipeline', value: 'nba_pipeline' }, { text: 'nba_long_regression_pipeline', value: 'nba_long_regression_pipeline' }]"
            :filter-method="filterPipeline">
            <template scope="scope">
              <el-tag :type="scope.row.pipeline === 'nba_pipeline' ? 'primary' : 'success'" close-transition>{{scope.row.pipeline}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="type" label="Type" width="110"
            :filters="[{ text: 'product', value: 'product' }, { text: 'environment', value: 'environment' }, { text: 'unknown', value: 'unknown' }]"
            :filter-method="filterType">
            <template scope="scope">
              <el-tag :type="scope.row.type === 'product' ? 'primary' : 'success'" close-transition>{{scope.row.type}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="build" label="Build" width="75">
          </el-table-column>
          <el-table-column prop="appliance" label="Appliance" width="150">
          </el-table-column>
          <el-table-column prop="ticket" label="Ticket" width="140">
          </el-table-column>
          <el-table-column prop="rootcause" label="RootCause" show-overflow-tooltip>
          </el-table-column>
          <el-table-column label="Action" width="90">
          <template scope="scope">
            <el-button type="text">Edit</el-button>
          </template>
        </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
import _ from 'lodash'
import { CardModal } from 'vue-bulma-modal'
import {mapGetters} from 'vuex'

export default {
  components: {
    CardModal
  },
  data () {
    return {
      typeSelected: '',
      pipelineSelected: '',
      buildSelected: '',
      applianceSelected: '',
      builds: [],
      appliances: [],
      failureTypes: ['automation', 'environment', 'unknown'],
      cardModal: null,
      dialogFormVisible: false,
      pipelines: [
          { label: 'NBA Pipeline', name: 'nba_pipeline', deployName: 'nba_main_regression_deploy' },
          { label: 'NBA Long Regression', name: 'nba_long_regression_pipeline', deployName: 'nba_main_long_regression_deploy' }],
      tableData3: [{
        pipeline: 'nba_long_regression_pipeline',
        type: 'environment',
        build: '360',
        appliance: 'i113-eng142',
        ticket: '',
        rootcause: 'All the three machines failed due to the fixing of Chengdu docker acf issues.All of the machine are ok now.'
      }, {
        pipeline: 'nba_long_regression_pipeline',
        type: 'environment',
        build: '363',
        appliance: 'nbapp569',
        ticket: '239282',
        rootcause: 'eth1 connection'
      }, {
        pipeline: 'nba_pipeline',
        type: 'product',
        build: '2309',
        appliance: 'sclmifaappd01v06',
        ticket: 'APPSOL-56926',
        rootcause: '"sclmifaappd01v06 factory checkpoint always takes around 1 hour'
      }, {
        pipeline: 'nba_pipeline',
        type: 'environment',
        build: '2313',
        appliance: 'sclautappd01v11',
        ticket: 'APPSOL-57012',
        rootcause: '[Dashboard] dashboard root space 97% full'
      }],
      multipleSelection: []
    }
  },

  watch: {
    pipelineSelected: function (val) {
      if (val && val[0]) {
        this.getDeployApplianceStats()
      }
    },
    buildSelected: function () {
      this.appliances = _.map(_.find(this.failedDeploymentStats, v => v.triggerId === this.buildSelected).appliances, o => o.appliance)
    }
  },

  computed: {
    ...mapGetters({
      failedDeploymentStats: 'failedDeploymentStats'
    })
  },

  methods: {
    submit () {
      this.dialogFormVisible = false
    },
    cancel () {
      this.dialogFormVisible = false
    },
    getDeployApplianceStats (deployName) {
      this.$store.dispatch('getDeployApplianceStats',
        {
          'deployPipeline': this.pipelineSelected,
          'deployName': _.find(this.pipelines, pipeline => pipeline.name === this.pipelineSelected).deployName
        })
    },
    handleSelectionChange (val) {
      this.multipleSelection = val
    },
    filterPipeline (value, row) {
      return row.pipeline === value
    },
    filterType (value, row) {
      return row.type === value
    },
    openModal () {
      this.dialogFormVisible = true
    },
    closeModal () {
      this.dialogFormVisible = false
    }
  }
}
</script>
