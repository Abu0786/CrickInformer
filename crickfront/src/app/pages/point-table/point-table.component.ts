import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-point-table',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './point-table.component.html',
  styleUrl: './point-table.component.css',
})
export class PointTableComponent implements OnInit {
  pointTable: any[][] = [];
  pointTableRow: string[] = ["Position", "Player" , "Rating"];

  constructor(private _api: ApiService) {}

  ngOnInit(): void {
    this.loadTable();
  }

  loadTable() {
    this._api.getICCRankingTable().subscribe({
      next: (data) => {
        const tableData = data as any[][]; // Type assertion
        console.log(tableData);
        if (Array.isArray(tableData) && tableData.length > 0) {
           // Assign the headers from the first row
          this.pointTable = tableData.slice(0); // Assign the rest of the rows as data
        }
      },
      error: (err) => {
        console.error('Error loading table data', err);
      },
    });
  }
}
