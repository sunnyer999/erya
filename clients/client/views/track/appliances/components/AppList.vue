<template>
  <div >
    <nav class="level">
      <div class="level-left">
        <div class="level-item animated flipInX">
          <el-button-group>
            <el-button type="primary" icon="plus" @click="handleEdit()">
            </el-button>
            <el-button v-if="multipleSelection.length>0" type="primary" icon="share"></el-button>
            <el-button v-if="multipleSelection.length>0" type="primary" icon="delete"></el-button>
          </el-button-group>
        </div>
      </div>
      <div class="level-right">
        <div class="level-item">
          <p class="control has-icon">
            <input class="input" @input="filterAppsByName" type="text" placeholder="Appliance Name">
            <span class="icon is-small">
              <i class="fa fa-search"></i>
            </span>
          </p>
        </div>
      </div>
    </nav>
    <el-table class="margin" :data="appliances" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column property="hostName" label="Name" sortable>
      </el-table-column>
      <el-table-column property="specification.model" :filters="modelsFilter" label="Model">
      </el-table-column>
      <el-table-column property="specification.type" :filters="appTypesFilter" label="Type" show-overflow-tooltip>
      </el-table-column>
      <el-table-column label="Assigned" :filters="teamsFilter">
        <template scope="scope">{{ scope.row.reservation.team }}</template>
      </el-table-column>
      <el-table-column label="Operations" align='center'>
        <template scope="scope">
          <el-button type="text" @click="handleEdit(scope.$index, scope.row)">
            <i class="fa fa-pencil-square-o fa-lg" aria-hidden="true"></i>
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="block">
      <div class="columns">
        <div class="column is-offset-4">
          <el-pagination layout="prev, pager, next" :total="100" style="border-radius: 0px" :page-size="10"></el-pagination>
        </div>
      </div>
      <edit-app :visible='isEditAppliance' @ok="isEditAppliance = false" @cancel="isEditAppliance = false" @close="isEditAppliance = false"></edit-app>
    </div>
  </div>
</template>
<script>
  import _ from 'lodash'
  import EditApp from './EditApp'

  export default {
    data () {
      return {
        isEditAppliance: false,
        appliances: [],
        multipleSelection: [],
        models: [5230, 5330].sort(),
        teams: ['icebreaker', 'autobots'].sort(),
        appTypes: ['hardware', 'virtual machine'].sort()
      }
    },

    components: {
      EditApp
    },

    computed: {
      modelsFilter () {
        return _.map(this.models, model => { return {'text': model, 'value': model} })
      },

      teamsFilter () {
        return _.map(this.teams, team => { return {'text': team, 'value': team} })
      },

      appTypesFilter () {
        return _.map(['hardware', 'virtual machine'], type => { return {'text': type, 'value': type} })
      }
    },
    methods: {
      handleSelectionChange (val) {
        this.multipleSelection = val
      },

      filterAppsByName () {

      },

      filterAppsByModel () {

      },

      filterAppsByTeams () {

      },

      handleEdit (index, row) {
        this.isEditAppliance = true
        console.log(row)
        console.log(index)
      }
    },

    created () {
      this.appliances = _.map(_.range(6), (i) => {
        return {
          updatedAt: '2016-05-03',
          createdAt: '2016-05-03',
          hostName: 'nbapp5as' + i,
          lab: 'mtv',
          ip: '10.220.182.6',
          specification: {
            model: '5230',
            type: 'hardware', // or vm
            sn: 'NNG05122510452'.trim().toLocaleLowerCase()
          },
          usageStats: {
            lastUpdated: '2016-05-03'
            // to add more fields
          },
          supportedScenarios: ['security', 'isci', '.....'],
          reservation: {
            from: '2016-05-03',
            to: '2017-05-03',
            piInfo: 'tbd',
            owner: 'dean.zou',
            pipeline: 'nba_pipeline', // if for dev, set null
            team: 'icebreaker', // team or pipeline
            justification: 'for nba pipeline'
          },
          status: 0, // implemented as enum, assigned or idle, adding, testing
          notes: 'blabla'
        }
      })
    }
  }
</script>

<style>
  .margin {
    margin-top: 40px;
    margin-bottom: 20px
  }
</style>
