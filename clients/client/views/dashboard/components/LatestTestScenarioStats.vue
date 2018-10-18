<template>
  <div id="latest-test-scenario">
    <div class="columns">
      <div class="column is-3">
        <div class="message animated fadeIn card">
          <pass-rate-pie :title='passRateTitle' :passRate='totalPassRate' :height=240></pass-rate-pie>
        </div>
        <div class="message animated fadeIn cases">
          <div class="columns">
            <div class="column is-half">
              <div class="notification is-blue">
                <i class="fa fa-check-circle fa-lg" aria-hidden="true"></i>
                {{passedTC}}
              </div>
            </div>
            <div class="column is-half">
              <div class="notification is-danger">
                <i class="fa fa-times-circle fa-lg" aria-hidden="true"></i>
                {{failedTC}}
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="column is-9">
        <div class="card">
          <pipeline-trend :pipelineName='pipelineName' @triggerIdSelected='getStats'></pipeline-trend>
        </div>
      </div>
    </div>
    <el-table :data="filteredScenarios" :default-sort="{prop: 'passRate', order: 'aescending'}" stripe align="left" boarder>
      <el-table-column type="index">
        <template scope="scope">
          <el-tag class="tag is-light is-medium">
            <a :href="scope.row.uri">{{scope.$index+1}}</a>
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="Scenario" sortable>
        <template scope="scope">
          <el-popover trigger="hover" placement="top">
            <p>{{ scope.row.name}}</p>
            <div slot="reference" class="name-wrapper">
              <el-tag class="tag is-light is-medium"><strong>{{ scope.row.name | truncate }}</strong></el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column prop="owner" label="Owner" sortable>
        <template scope="scope">
          <el-tag class="fa fa-address-book-o tag is-light is-medium">&nbsp;{{ scope.row.owner}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="passRate" label="Pass" sortable>
        <template scope="scope">
          <el-progress :text-inside="true" :stroke-width=18 :status="scope.row.passRate>95? '':'exception'" :percentage="scope.row.passRate"></el-progress>
        </template>
      </el-table-column>
      <el-table-column prop="elapse" label="Elapse" width=100 sortable>
        <template scope="scope">
          <span v-if="scope.row.elapse > 10800000" class="tag is-warning is-medium">{{ scope.row.elapse | duration('h:mm:ss')}}</span>
          <span v-else class="tag is-light is-medium">{{ scope.row.elapse | duration('h:mm:ss')}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="failedCases" label="Failed" width=100 sortable>
      </el-table-column>
      <el-table-column prop="executedCases" label="Executed" sortable width=120>
      </el-table-column>
    </el-table>
  </div>
</template>
<script>
  import { mapGetters } from 'vuex'
  import _ from 'lodash'
  import PassRatePie from './PassRatePie'
  import PipelineTrend from './PipelineTrend'

  export default {
    props: {
      title: {
        type: String,
        required: true
      },
      pipelineName: {
        type: String,
        required: true
      }
    },

    components: {
      PassRatePie,
      PipelineTrend
    },

    computed: {
      ...mapGetters({
        latestTestScenarioStats: 'latestTestScenarioStats'
      }),

      passRateTitle () {
        return '#' + this.triggerId
      },

      triggerId () {
        return this.stats.triggerId || ''
      },

      stats () {
        return this.latestTestScenarioStats[this.pipelineName] || {}
      },

      scenarios () {
        if (!this.latestTestScenarioStats[this.pipelineName]) return []
        return this.latestTestScenarioStats[this.pipelineName].scenarios || []
      },

      filteredScenarios () {
        if (!this.scenarioFilter) {
          return this.scenarios
        }
        return _.filter(this.scenarios, test => {
          return _.find(_.map(_.values(test), _.toString), v => _.toLower(v).includes(_.toLower(this.scenarioFilter)))
        })
      },

      passedTC () {
        return this.stats.totalPassedCases || 0
      },

      failedTC () {
        return this.stats.totalExpectedCases - this.stats.totalPassedCases
      },

      totalPassRate () {
        if (!this.stats.totalExpectedCases) {
          return 0
        }
        return Math.floor(this.passedTC / this.stats.totalExpectedCases * 100)
      }
    },

    data () {
      return {
        scenarioFilter: '',
        builds: 3
      }
    },

    methods: {
      getLatestStats (pipelineName) {
        this.$store.dispatch('getTestScenariosLatest', {'pipeline': pipelineName})
      },

      getStats (triggerId) {
        this.$store.dispatch('getTestScenariosLatest', {'pipeline': this.pipelineName, 'triggerId': triggerId})
      },

      inputTestScenarioFilter: _.debounce(function (e) {
        this.scenarioFilter = e.target.value
      }, 350)
    },

    watch: {
      pipelineName (value) {
        this.getLatestStats(value)
      }
    },

    created () {
      this.getLatestStats(this.pipelineName)
    }
  }

</script>

<style>
  #latest-test-scenario {
    margin-top: 20px
  }

  .cases {
    background-color: white
  }

  .is-blue {
    background-color: #4DD0E1;
    color: white;
    
  }
  .notification {
    font-size: medium
  }
</style>
